package com.github.kamil1338.classifying_ui

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.activity_recognition_core.core.SensorData
import com.github.kamil1338.classifying_usecase.classifying.ClassificationData
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase
import com.github.kamil1338.classifying_usecase.sound.SoundUseCase
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCase
import com.github.kamil1338.recording_app.power_usecase.PowerUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ClassifyingViewModel @Inject constructor(
    private val sensorUseCase: SensorUseCase,
    private val classifyingUseCase: ClassifyingUseCase,
    private val soundUseCase: SoundUseCase,
    private val powerUseCase: PowerUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun startClassifying() {
        executePowerUseCase()
        disposables +=
            sensorUseCase.execute()
                .buffer(2, TimeUnit.SECONDS)
                .map { sensorData ->
                    sensorData.map { it.toClassificationData() }
                }
                .flatMap {
                    classifyingUseCase.execute(it)
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::handleClassificationResult)
    }

    private fun executePowerUseCase() {
        disposables += powerUseCase.execute()
            .subscribeOn(Schedulers.computation())
            .subscribe()
    }

    private fun handleClassificationResult(activityType: ActivityType) {
        when (activityType) {
            ActivityType.Other -> soundUseCase.playSoundOther()
            ActivityType.Running -> soundUseCase.playSoundRunning()
            ActivityType.Walking -> soundUseCase.playSoundWalking()
        }
    }
}

fun SensorData.toClassificationData(): ClassificationData {
    return ClassificationData(x, y, z)
}