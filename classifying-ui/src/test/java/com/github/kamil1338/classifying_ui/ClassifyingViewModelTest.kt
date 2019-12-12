package com.github.kamil1338.classifying_ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.activity_recognition_core.core.SensorData
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase
import com.github.kamil1338.classifying_usecase.sound.SoundUseCase
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCase
import com.github.kamil1338.recording_app.power_usecase.PowerUseCase
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


class ClassifyingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    val testScheduler = TestScheduler()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule(testScheduler)

    val sensorUseCase: SensorUseCase = mock()
    val classifyingUseCase: ClassifyingUseCase = mock()
    val soundUseCase: SoundUseCase = mock()
    val powerUseCase: PowerUseCase = mock {
        on { execute() } doReturn Observable.just(Unit)
    }

    val tested = ClassifyingViewModel(sensorUseCase, classifyingUseCase, soundUseCase, powerUseCase)

    @Test
    fun `given classification when started then finishes successfully`() {
        // given
        val value = 3.14f
        val sensorData = SensorData(value, value, value)
        whenever(sensorUseCase.execute()).doReturn(Observable.just(sensorData))
        whenever(classifyingUseCase.execute(any()))
            .doReturn(Observable.just(ActivityType.Other))

        // when
        tested.startClassifying()

        // then
        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        verify(sensorUseCase, times(1)).execute()
        verify(classifyingUseCase, times(1)).execute(any())
        verify(powerUseCase, times(1)).execute()
    }

    @Test
    fun `given sensor data when sending new events then all sensor data are classified`() {
        // given
        val value = 3.14f
        val sensorData = SensorData(value, value, value)
        whenever(sensorUseCase.execute()).doReturn(
            Observable.just(
                sensorData,
                sensorData,
                sensorData
            )
        )
        whenever(classifyingUseCase.execute(any()))
            .doReturn(Observable.just(ActivityType.Other))

        // when
        tested.startClassifying()

        // then
        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        verify(sensorUseCase, times(1)).execute()
        verify(classifyingUseCase, times(1)).execute(any())
        verify(powerUseCase, times(1)).execute()
    }

}