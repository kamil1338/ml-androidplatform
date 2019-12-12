package com.github.kamil1338.recording_app.collecting_use_case

import android.app.Notification
import android.app.NotificationChannel
import androidx.core.app.NotificationManagerCompat

open class NotificationManagerWrapper(
    private val notificationManager: NotificationManagerCompat
) {

    fun createNotificationChannel(notificationChannel: NotificationChannel) {
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun notify(notificationId: Int, notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    fun cancel(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}