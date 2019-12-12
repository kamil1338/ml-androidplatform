package com.github.kamil1338.recording_app.collecting_ui.collecting

import com.github.kamil1338.activity_recognition_core.core.ActivityType

data class Config(
    val durationInSec: Long,
    val intervals: Long,
    val activityType: ActivityType,
    val executionDelayInSec: Long
)