package com.github.kamil1338.recording_app.storage_use_case.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Recording.TABLE_NAME)
data class Recording(
    @ColumnInfo(name = "recording_id") val recordingId: String,
    @ColumnInfo(name = "save_time") val saveTimeFormatted: String,
    @ColumnInfo(name = Recording.ACTIVITY_NAME) val activity: String,
    @ColumnInfo(name = Recording.COUNT) val count: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "Recording"
        const val ACTIVITY_NAME = "activity"
        const val COUNT = "count"
    }
}