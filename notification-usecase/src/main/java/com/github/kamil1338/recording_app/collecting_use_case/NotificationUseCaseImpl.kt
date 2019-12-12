package com.github.kamil1338.recording_app.collecting_use_case

import android.app.NotificationChannel

class NotificationUseCaseImpl(
    private val notificationManagerWrapper: NotificationManagerWrapper,
    private val summaryChannel: NotificationChannel,
    private val savingChannel: NotificationChannel,
    private val workInProgressChannel: NotificationChannel,
    private val notificationProvider: NotificationProvider
) : NotificationUseCase {

    private val workInProgressNotificationId: Int = generateId()

    init {
        notificationManagerWrapper.createNotificationChannel(summaryChannel)
        notificationManagerWrapper.createNotificationChannel(savingChannel)
        notificationManagerWrapper.createNotificationChannel(workInProgressChannel)
    }

    override fun sendStartCollectingNotification(notificationId: Int) {
        val builder = notificationProvider.createSummaryNotification()
        notificationManagerWrapper.notify(notificationId, builder.build())
    }

    override fun sendWriteToDbNotification(notificationId: Int, savedCount: Int) {
        val builder = notificationProvider.createSuccessWriteToDbNotification(savedCount)
        notificationManagerWrapper.notify(notificationId, builder.build())
    }

    override fun sendWorkInProgressNotification() {
        val builder = notificationProvider.createWorkInProgressStartedNotification()
        notificationManagerWrapper.notify(workInProgressNotificationId, builder.build())
    }

    override fun cancelBackgroundWorkNotification() {
        notificationManagerWrapper.cancel(workInProgressNotificationId)
        val builder = notificationProvider.createWorkInProgressIsDoneNotification()
        notificationManagerWrapper.notify(workInProgressNotificationId, builder.build())
    }

    private fun generateId(): Int {
        return System.currentTimeMillis().toInt()
    }
}