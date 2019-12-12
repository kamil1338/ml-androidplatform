package com.github.kamil1338.recording_app.storage_use_case

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import io.reactivex.Single

class DataCountFetchUseCaseImpl(
    private val accelerometerDataDao: AccelerometerDataDao
) : DataCountUseCase {

    override fun dataCount(activityType: ActivityType): Single<Long> =
        Single.fromCallable {
            accelerometerDataDao.getDataCountFor(activityType.name)
        }

    override fun recordingCount(activityType: ActivityType): Single<Long> =
        Single.fromCallable {
            accelerometerDataDao.getRecordingCountFor(activityType.name)
        }
}