package com.github.kamil1338.recording_app.storage_use_case

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.activity_recognition_core.core.SensorData

interface SavingUseCase {
    fun saveRecordingData(recordingId: String, activityType: ActivityType, data: List<SensorData>)
    fun saveNewRecording(recordingId: String, saveTimeInMillis: Long, activity: String, count: Int)
}