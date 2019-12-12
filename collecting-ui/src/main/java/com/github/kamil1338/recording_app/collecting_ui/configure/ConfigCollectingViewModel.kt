package com.github.kamil1338.recording_app.collecting_ui.configure

import android.widget.SeekBar
import androidx.lifecycle.ViewModel
import com.github.kamil1338.activity_recognition_core.core.util.mutableLiveData
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.recording_app.collecting_ui.collecting.Config
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.math.min

internal typealias Lis = SeekBar.OnSeekBarChangeListener

class ConfigCollectingViewModel @Inject constructor() : ViewModel() {

    private val disposables = CompositeDisposable()

    val durationValue = mutableLiveData("")
    private val _durationValue = mutableLiveData(INITIAL_DURATION_IN_SEC)

    val durationChangeListener = mutableLiveData<Lis>(object : SeekBarLis {
        override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                _durationValue.value = min(MIN_DURATION_IN_SEC + progress, MAX_DURATION_IN_SEC)
            }
        }
    })

    val maxDuration = mutableLiveData(MAX_DURATION_IN_SEC)

    val intervalValue = mutableLiveData("")
    private val _intervalValue = mutableLiveData(INITIAL_INTERVAL_COUNT)

    val intervalChangeListener = mutableLiveData<Lis>(object : SeekBarLis {
        override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                _intervalValue.value = min(MIN_INTERVAL_COUNT + progress, MAX_INTERVAL_COUNT)
            }
        }
    })

    val maxInterval = mutableLiveData(MAX_INTERVAL_COUNT)

    val delayValue = mutableLiveData("")
    private val _delayValue = mutableLiveData(INITIAL_DELAY_COUNT)

    val delayChangeListener = mutableLiveData<Lis>(object : SeekBarLis {
        override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                _delayValue.value = min(MIN_DELAY_IN_SEC + progress, MAX_DELAY_IN_SEC)
            }
        }
    })

    val maxDelay = mutableLiveData(MAX_DELAY_IN_SEC)

    val runningChecked = mutableLiveData(true)
    val walkingChecked = mutableLiveData(false)
    val otherChecked = mutableLiveData(false)

    init {
        _durationValue.observeForever { durationValue.value = "$it seconds" }
        _intervalValue.observeForever { intervalValue.value = "$it intervals" }
        _delayValue.observeForever { delayValue.value = "$it seconds" }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun onRunningButtonPressed() {
        runningChecked.value = true
        walkingChecked.value = false
        otherChecked.value = false
    }

    fun onWalkingButtonPressed() {
        runningChecked.value = false
        walkingChecked.value = true
        otherChecked.value = false
    }

    fun onOtherButtonPressed() {
        runningChecked.value = false
        walkingChecked.value = false
        otherChecked.value = true
    }

    private fun provideActivityTypeFromInnerState(): ActivityType = when {
        runningChecked.value -> ActivityType.Running
        walkingChecked.value -> ActivityType.Walking
        else -> ActivityType.Other
    }

    fun toUseCaseConfig(): Config {
        return Config(
            durationInSec = _durationValue.value.toLong(),
            intervals = _intervalValue.value.toLong(),
            activityType = provideActivityTypeFromInnerState(),
            executionDelayInSec = _delayValue.value.toLong()
        )
    }

    companion object {
        const val MIN_DURATION_IN_SEC = 2
        const val MAX_DURATION_IN_SEC = 30
        const val INITIAL_DURATION_IN_SEC = MIN_DURATION_IN_SEC

        const val MIN_INTERVAL_COUNT = 1
        const val MAX_INTERVAL_COUNT = 60
        const val INITIAL_INTERVAL_COUNT = MIN_INTERVAL_COUNT

        const val MIN_DELAY_IN_SEC = 4
        const val MAX_DELAY_IN_SEC = 20
        const val INITIAL_DELAY_COUNT = MIN_DELAY_IN_SEC
    }
}

internal interface SeekBarLis : Lis {
    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {}
}
