package com.github.kamil1338.recording_app.storage_use_case

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.nhaarman.mockitokotlin2.*
import org.junit.Test

class DataCountFetchUseCaseTest {

    val accelerometerDataDao: AccelerometerDataDao = mock()

    val tested = DataCountFetchUseCaseImpl(accelerometerDataDao)

    @Test
    fun `given data count when execute then invoke correct method`() {
        // given
        whenever(accelerometerDataDao.getDataCountFor(any())).doReturn(1)

        // when
        tested.dataCount(ActivityType.Other).test()

        verify(accelerometerDataDao).getDataCountFor(any())
    }

    @Test
    fun `given recording count when executed then invoke correct method`() {
        // given
        whenever(accelerometerDataDao.getRecordingCountFor(any())).doReturn(1)

        // when
        tested.recordingCount(ActivityType.Other).test()

        verify(accelerometerDataDao).getRecordingCountFor(any())
    }
}