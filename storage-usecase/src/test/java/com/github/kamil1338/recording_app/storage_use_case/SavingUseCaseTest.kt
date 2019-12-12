package com.github.kamil1338.recording_app.storage_use_case

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SavingUseCaseTest {

    val accelerometerDataDao: AccelerometerDataDao = mock()

    val tested = SavingUseCaseImpl(accelerometerDataDao)

    @Test
    fun `given insert data when new recording is saved then dao method is invoked`() {
        // given

        // when
        tested.saveNewRecording("", 0, "", 0)

        // then
        verify(accelerometerDataDao).insertRecording(any())
    }

    @Test
    fun `given recording data when invoked then dao method is invoked`() {
        // given

        // when
        tested.saveRecordingData("", ActivityType.Other, listOf())

        // then
        verify(accelerometerDataDao, times(1)).insertAccelerometerData(any())
    }
}