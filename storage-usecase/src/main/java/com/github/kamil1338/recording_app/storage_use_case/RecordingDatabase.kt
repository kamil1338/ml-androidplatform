package com.github.kamil1338.recording_app.storage_use_case

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.kamil1338.recording_app.storage_use_case.entity.*

@Database(
    entities = [
        Recording::class,
        AccelerometerData::class
    ],
    version = 1
)
abstract class RecordingDatabase : RoomDatabase() {

    abstract fun accelerometerDao(): AccelerometerDataDao
}