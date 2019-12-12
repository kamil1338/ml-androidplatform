package com.github.kamil1338.recording_app.storage_use_case.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AccelerometerData.TABLE_NAME)
data class AccelerometerData(
    @ColumnInfo(name = "recording_id") val recordingId: String,
    @ColumnInfo(name = X_DATA_NAME) val xData: Float,
    @ColumnInfo(name = Y_DATA_NAME) val yData: Float,
    @ColumnInfo(name = Z_DATA_NAME) val zData: Float,
    @ColumnInfo(name = ACTIVITY_NAME) val activity: String,
    @ColumnInfo(name = "time") val timeOrder: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val TABLE_NAME = "AccelerometerData"
        const val X_DATA_NAME = "x_data"
        const val Y_DATA_NAME = "y_data"
        const val Z_DATA_NAME = "z_data"
        const val ACTIVITY_NAME = "activity"
    }
}