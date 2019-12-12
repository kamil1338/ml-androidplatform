package com.github.kamil1338.recording_app.collecting_use_case.sensor

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCaseImpl
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SensorUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule()

    val sensorManager: SensorManager = mock()
    val sensor: Sensor = mock()

    val tested = SensorUseCaseImpl(
        sensorManager,
        sensor
    )

    @Test
    fun `given wrapper when executed and stopped then completes successfully`() {
        // given

        // when
        val observable = tested.execute().test()
        observable.dispose()

        // then
        verify(sensorManager).registerListener(any(), eq(sensor), any())
        verify(sensorManager).unregisterListener(any(), eq(sensor))
        Assert.assertTrue(observable.isDisposed)
    }

    @Test
    fun `given wrapper when executed and not stopped then doesn't stop`() {
        // given

        // when
        val observable = tested.execute().test()

        // then
        Assert.assertFalse(observable.isDisposed)
        verify(sensorManager).registerListener(any(), eq(sensor), any())
        verify(sensorManager, times(0)).unregisterListener(any(), eq(sensor))
        Assert.assertFalse(observable.isDisposed)
    }

    @Test
    fun `given wrapper when not executed then doesn't start`() {
        // given

        // when

        // then
        verify(sensorManager, times(0)).registerListener(any(), eq(sensor), any())
        verify(sensorManager, times(0)).unregisterListener(any(), eq(sensor))
    }
}