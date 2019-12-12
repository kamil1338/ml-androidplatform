package com.github.kamil1338.recording_app.collecting_ui.get_started

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.kamil1338.activity_recognition_core.core.util.mutableLiveData
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.recording_app.storage_use_case.DataCountUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CollectingGetStartedViewModel @Inject constructor(
    private val dataCountFetchUseCase: DataCountUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _walkingDataCount = mutableLiveData(0L)
    private val _runningDataCount = mutableLiveData(0L)
    private val _otherDataCount = mutableLiveData(0L)

    val walkingDataCount = mutableLiveData("")
    val runningDataCount = mutableLiveData("")
    val otherDataCount = mutableLiveData("")

    private val _walkingRecordingCount = mutableLiveData(0L)
    private val _runningRecordingCount = mutableLiveData(0L)
    private val _otherRecordingCount = mutableLiveData(0L)

    private val disposables = CompositeDisposable()

    init {
        bindDataConversions()
        bindRecordingConversions()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        fetchDataCounts()
        fetchRecordingCounts()
    }

    private fun bindDataConversions() {
        _runningDataCount.observeForever {
            runningDataCount.value = formatValue(it, _runningRecordingCount.value)
        }
        _walkingDataCount.observeForever {
            walkingDataCount.value = formatValue(it, _walkingRecordingCount.value)
        }
        _otherDataCount.observeForever {
            otherDataCount.value = formatValue(it, _otherRecordingCount.value)
        }
    }

    private fun bindRecordingConversions() {
        _runningRecordingCount.observeForever {
            runningDataCount.value = formatValue(_runningDataCount.value, it)
        }
        _walkingRecordingCount.observeForever {
            walkingDataCount.value = formatValue(_walkingDataCount.value, it)
        }
        _otherRecordingCount.observeForever {
            otherDataCount.value = formatValue(_otherDataCount.value, it)
        }
    }

    private fun formatValue(data: Long, recording: Long) = "$data/$recording"


    private fun fetchDataCounts() {
        fetchDataCount(ActivityType.Running) { _runningDataCount.value = it }
        fetchDataCount(ActivityType.Walking) { _walkingDataCount.value = it }
        fetchDataCount(ActivityType.Other) { _otherDataCount.value = it }
    }

    private fun fetchDataCount(activityType: ActivityType, success: (Long) -> Unit) {
        disposables += dataCountFetchUseCase.dataCount(activityType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(success)
    }

    private fun fetchRecordingCounts() {
        fetchRecordingCount(ActivityType.Running) { _runningRecordingCount.value = it }
        fetchRecordingCount(ActivityType.Walking) { _walkingRecordingCount.value = it }
        fetchRecordingCount(ActivityType.Other) { _otherRecordingCount.value = it }
    }

    private fun fetchRecordingCount(activityType: ActivityType, success: (Long) -> Unit) {
        disposables += dataCountFetchUseCase.recordingCount(activityType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(success)
    }

}