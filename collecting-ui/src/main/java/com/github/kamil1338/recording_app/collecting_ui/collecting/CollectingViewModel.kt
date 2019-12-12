package com.github.kamil1338.recording_app.collecting_ui.collecting

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.activity_recognition_core.core.SensorData
import com.github.kamil1338.recording_app.collecting_use_case.NotificationUseCase
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCase
import com.github.kamil1338.recording_app.power_usecase.PowerUseCase
import com.github.kamil1338.recording_app.storage_use_case.SavingUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CollectingViewModel @Inject constructor(
    private val savingUseCase: SavingUseCase,
    private val notificationUseCase: NotificationUseCase,
    private val sensorUseCase: SensorUseCase,
    private val powerUseCase: PowerUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val disposables = CompositeDisposable()
    private val collectingChecker = CollectingChecker()

    private val workNotStartedYet: Boolean
        get() = collectingChecker.workNotStartedYet

    private val isAllWorkDone: Boolean
        get() = collectingChecker.isAllWorkDone

    private var baseNotificationId = 0

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun startCollectingData(config: Config) {
        if (workNotStartedYet) {
            doFirstTimeInit()
        }
        val notificationId = baseNotificationId++
        notifyNextCollecting()
        disposables +=
            Observable.just(Unit)
                .delay(config.executionDelayInSec, TimeUnit.SECONDS)
                .flatMap {
                    doPreCollectingInit(notificationId)
                    sensorUseCase.execute()
                        .buffer(config.durationInSec, TimeUnit.SECONDS)
                        .take(config.intervals)
                }
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe({
                    onNewData(notificationId, config, it)
                }, {}, {
                    onCollectingFinished()
                })
    }

    private fun doFirstTimeInit() {
        sendWorkInProgressNotification()
        executePowerUseCase()
    }

    private fun sendWorkInProgressNotification() {
        notificationUseCase.sendWorkInProgressNotification()
    }

    private fun executePowerUseCase() {
        disposables += powerUseCase.execute()
            .subscribeOn(Schedulers.computation())
            .subscribe()
    }

    private fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    private fun notifyNextCollecting() {
        collectingChecker.notifyNextCollecting()
    }

    private fun doPreCollectingInit(notificationId: Int) {
        sendStartCollectingNotification(notificationId)
    }

    private fun sendStartCollectingNotification(notificationId: Int) {
        notificationUseCase.sendStartCollectingNotification(notificationId)
    }

    private fun onNewData(notificationId: Int, config: Config, data: List<SensorData>) {
        val recordingId = generateId()
        saveNewRecording(recordingId, config.activityType.name, data.size)
        saveRecordingData(recordingId, config.activityType, data)
        sendSuccessWriteDataNotification(notificationId, data.size)
    }

    private fun saveNewRecording(recordingId: String, activity: String, count: Int) {
        val saveTimeInMillis = System.currentTimeMillis()
        savingUseCase.saveNewRecording(recordingId, saveTimeInMillis, activity, count)
    }

    private fun saveRecordingData(
        recordingId: String,
        activityType: ActivityType,
        data: List<SensorData>
    ) {
        savingUseCase.saveRecordingData(recordingId, activityType, data)
    }

    private fun sendSuccessWriteDataNotification(notificationId: Int, dataSize: Int) {
        notificationUseCase.sendWriteToDbNotification(notificationId, dataSize)
    }

    private fun onCollectingFinished() {
        notifyFinishCollecting()
        if (isAllWorkDone) {
            cancelWorkInProgressNotification()
            resetChecker()
        }
    }

    private fun notifyFinishCollecting() {
        collectingChecker.notifyNextFinish()
    }

    private fun cancelWorkInProgressNotification() {
        notificationUseCase.cancelBackgroundWorkNotification()
    }

    private fun resetChecker() {
        collectingChecker.reset()
    }
}