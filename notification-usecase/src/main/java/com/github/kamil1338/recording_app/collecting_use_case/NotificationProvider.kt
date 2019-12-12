package com.github.kamil1338.recording_app.collecting_use_case

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.notification_usecase.R

open class NotificationProvider(
    private val applicationContext: Context,
    private val summaryChannelId: String,
    private val savingChannelId: String,
    private val workInProgressChannelId: String
) {

    fun createSummaryNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, summaryChannelId)
            .setSmallIcon(R.drawable.ic_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle("Collecting")

    fun createSuccessWriteToDbNotification(savedCount: Int): NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, savingChannelId)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle("Data saved in database")
            .setContentText("Saved $savedCount probes")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    fun createWorkInProgressStartedNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, workInProgressChannelId)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle("Work in progress")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)

    fun createWorkInProgressIsDoneNotification(): NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, workInProgressChannelId)
            .setSmallIcon(R.drawable.ic_icon)
            .setContentTitle("Work is done")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
}