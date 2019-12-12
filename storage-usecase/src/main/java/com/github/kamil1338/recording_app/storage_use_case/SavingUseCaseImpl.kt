package com.github.kamil1338.recording_app.storage_use_case

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import com.github.kamil1338.activity_recognition_core.core.SensorData
import com.github.kamil1338.recording_app.storage_use_case.entity.*
import java.text.SimpleDateFormat
import java.util.*

class SavingUseCaseImpl(
    private val accelerometerDataDao: AccelerometerDataDao
) : SavingUseCase {

    override fun saveRecordingData(
        recordingId: String,
        activityType: ActivityType,
        data: List<SensorData>
    ) {
        data.mapIndexed { index, probe ->
            AccelerometerData(recordingId, probe.x, probe.y, probe.z, activityType.name, index)
        }.toList().also {
            accelerometerDataDao.insertAccelerometerData(it)
        }
    }

    override fun saveNewRecording(recordingId: String, saveTimeInMillis: Long, activity: String, count: Int) {
        val dateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val formattedTime = dateFormat.format(Calendar.getInstance().time)
        val recording = Recording(recordingId, formattedTime, activity, count)
        accelerometerDataDao.insertRecording(recording)
    }
}