package com.github.kamil1338.recording_app.collecting_ui.configure

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class ConfigCollectingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule()

    @Test
    fun `given duration value when value is initialized then emmit formatted string`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerDuration: Observer<String> = mock()
        tested.durationValue.observeForever(observerDuration)

        // when

        // then
        verify(observerDuration, times(1)).onChanged(any())
    }

    @Test
    fun `given duration value when value changes then emmit formatted string`() {
        // given
        val tested = ConfigCollectingViewModel()
        val progress = 1
        val observerDuration: Observer<String> = mock()
        tested.durationValue.observeForever(observerDuration)

        // when
        tested.durationChangeListener.value.onProgressChanged(mock(), progress, true)

        // then
        verify(observerDuration, times(2)).onChanged(any())
    }

    @Test
    fun `given interval value when value is initialized then emit formatted string`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerInterval: Observer<String> = mock()
        tested.intervalValue.observeForever(observerInterval)

        // when

        // then
        verify(observerInterval, times(1)).onChanged(any())
    }

    @Test
    fun `given interval value when value changes then emit formatted string`() {
        // given
        val tested = ConfigCollectingViewModel()
        val progress = 1
        val observerInterval: Observer<String> = mock()
        tested.intervalValue.observeForever(observerInterval)

        // when
        tested.intervalChangeListener.value.onProgressChanged(mock(), progress, true)

        // then
        verify(observerInterval, times(2)).onChanged(any())
    }

    @Test
    fun `given delay value when value is initialized then emmit formatted string`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerDelay: Observer<String> = mock()
        tested.delayValue.observeForever(observerDelay)

        // when

        // then
        verify(observerDelay, times(1)).onChanged(any())
    }

    @Test
    fun `given delay value when value changes then emmit formatted string`() {
        // given
        val tested = ConfigCollectingViewModel()
        val progress = 1
        val observerDelay: Observer<String> = mock()
        tested.delayValue.observeForever(observerDelay)

        // when
        tested.delayChangeListener.value.onProgressChanged(mock(), progress, true)

        // then
        verify(observerDelay, times(2)).onChanged(any())
    }

    @Test
    fun `given running value when value is initialized then emit true`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerRunning: Observer<Boolean> = mock()
        tested.runningChecked.observeForever(observerRunning)

        // when

        // then
        verify(observerRunning, times(1)).onChanged(true)
    }

    @Test
    fun `given walking value when value is initialized then emit false`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerRunning: Observer<Boolean> = mock()
        tested.walkingChecked.observeForever(observerRunning)

        // when

        // then
        verify(observerRunning, times(1)).onChanged(false)
    }

    @Test
    fun `given other value when value is initialized then emit false`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerRunning: Observer<Boolean> = mock()
        tested.otherChecked.observeForever(observerRunning)

        // when

        // then
        verify(observerRunning, times(1)).onChanged(false)
    }

    @Test
    fun `given running when checked then walking and other are unchecked`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerRunning: Observer<Boolean> = mock()
        val observerWalking: Observer<Boolean> = mock()
        val observerOther: Observer<Boolean> = mock()
        tested.runningChecked.observeForever(observerRunning)
        tested.walkingChecked.observeForever(observerWalking)
        tested.otherChecked.observeForever(observerOther)

        // when
        tested.onRunningButtonPressed()

        // then
        verify(observerRunning, times(2)).onChanged(true)
        verify(observerWalking, times(2)).onChanged(false)
        verify(observerOther, times(2)).onChanged(false)
    }

    @Test
    fun `given walking when checked then running and other are unchecked`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerRunning: Observer<Boolean> = mock()
        val observerWalking: Observer<Boolean> = mock()
        val observerOther: Observer<Boolean> = mock()
        tested.runningChecked.observeForever(observerRunning)
        tested.walkingChecked.observeForever(observerWalking)
        tested.otherChecked.observeForever(observerOther)

        // when
        tested.onWalkingButtonPressed()

        // then
        verify(observerRunning).onChanged(false)
        verify(observerWalking).onChanged(true)
        verify(observerOther, times(2)).onChanged(false)
    }

    @Test
    fun `given other when checked then running and walking are unchecked`() {
        // given
        val tested = ConfigCollectingViewModel()
        val observerRunning: Observer<Boolean> = mock()
        val observerWalking: Observer<Boolean> = mock()
        val observerOther: Observer<Boolean> = mock()
        tested.runningChecked.observeForever(observerRunning)
        tested.walkingChecked.observeForever(observerWalking)
        tested.otherChecked.observeForever(observerOther)

        // when
        tested.onOtherButtonPressed()

        // then
        verify(observerRunning).onChanged(false)
        verify(observerWalking, times(2)).onChanged(false)
        verify(observerOther).onChanged(true)
    }

    @Test
    fun `given running checked when converting to use case config then returned config contains running activity type`() {
        // given
        val tested = ConfigCollectingViewModel()
        tested.onRunningButtonPressed()

        // when
        val createdActivityType = tested.toUseCaseConfig()

        // then
        Assert.assertEquals(ActivityType.Running, createdActivityType.activityType)
    }

    @Test
    fun `given walking checked when converting to use case config then returned config contains walking activity type`() {
        // given
        val tested = ConfigCollectingViewModel()
        tested.onWalkingButtonPressed()

        // when
        val createdActivityType = tested.toUseCaseConfig()

        // then
        Assert.assertEquals(ActivityType.Walking, createdActivityType.activityType)
    }

    @Test
    fun `given other checked when converting to use case config then returned config contains other activity type`() {
        // given
        val tested = ConfigCollectingViewModel()
        tested.onOtherButtonPressed()

        // when
        val createdActivityType = tested.toUseCaseConfig()

        // then
        Assert.assertEquals(ActivityType.Other, createdActivityType.activityType)
    }
}