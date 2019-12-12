package com.github.kamil1338.recording_app.storage_use_case

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import io.reactivex.Single

interface DataCountUseCase {

    fun dataCount(activityType: ActivityType): Single<Long>
    fun recordingCount(activityType: ActivityType): Single<Long>
}