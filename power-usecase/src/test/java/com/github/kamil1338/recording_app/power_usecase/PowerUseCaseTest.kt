package com.github.kamil1338.recording_app.power_usecase

import android.os.PowerManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.nhaarman.mockitokotlin2.*
import org.junit.Rule
import org.junit.Test

class PowerUseCaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule()

    val wakeLock: PowerManager.WakeLock = mock()
    val powerManager: PowerManager = mock {
        on { newWakeLock(any(), any()) } doReturn wakeLock
    }

    val tested = PowerUseCaseImpl(powerManager)

    @Test
    fun `given use case when executed then wake lock is acquired`() {
        // given

        // when
        tested.execute().test()

        // then
        verify(wakeLock).acquire()
    }

    @Test
    fun `given use case when disposed then releases wake lock`() {
        // given

        // when
        val observer = tested.execute().test()
        observer.dispose()

        // then
        verify(wakeLock).release()
    }
}