package com.github.kamil1338.recording_app.collecting_use_case

interface NotificationUseCase {
    fun sendStartCollectingNotification(notificationId: Int)
    fun sendWriteToDbNotification(notificationId: Int, savedCount: Int)
    fun sendWorkInProgressNotification()
    fun cancelBackgroundWorkNotification()
}