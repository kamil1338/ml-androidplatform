package com.github.kamil1338.recording_app.storage_use_case

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.kamil1338.recording_app.storage_use_case.entity.AccelerometerData
import com.github.kamil1338.recording_app.storage_use_case.entity.Recording

@Dao
interface AccelerometerDataDao {

    @Insert
    fun insertRecording(recording: Recording)

    @Insert
    fun insertAccelerometerData(data: List<AccelerometerData>)

    @Query(
        """
        SELECT COUNT(*) 
        FROM ${AccelerometerData.TABLE_NAME} 
        WHERE ${AccelerometerData.ACTIVITY_NAME}=:activity
        """
    )
    fun getDataCountFor(activity: String): Long

    @Query(
        """
        SELECT COUNT(*) 
        FROM ${Recording.TABLE_NAME}
        WHERE ${Recording.ACTIVITY_NAME}=:activity
        """
    )
    fun getRecordingCountFor(activity: String): Long
}