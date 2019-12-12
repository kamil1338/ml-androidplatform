package com.github.kamil1338.recording_app.collecting_use_case

import android.app.Notification
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import com.nhaarman.mockitokotlin2.*
import org.junit.Test


class NotificationUseCaseTest {

    val notificationId = 123
    val notification = Notification()

    val summaryChannel: NotificationChannel = mock()
    val savingChannel: NotificationChannel = mock()
    val workInProgressChannel: NotificationChannel = mock()
    val notificationProvider: NotificationProvider = mock()
    val notificationManagerWrapper: NotificationManagerWrapper = mock()
    val builder: NotificationCompat.Builder = mock {
        on { build() } doReturn notification
    }

    val tested = NotificationUseCaseImpl(
        notificationManagerWrapper,
        summaryChannel,
        savingChannel,
        workInProgressChannel,
        notificationProvider
    )

    @Test
    fun `given success write notification when send start collecting notification then create notification and notify manager`() {
        // given
        whenever(notificationProvider.createSuccessWriteToDbNotification(0))
            .doReturn(builder)

        // when
        tested.sendWriteToDbNotification(notificationId, 0)

        // then
        verify(notificationProvider).createSuccessWriteToDbNotification(any())
        verify(builder).build()
        verify(notificationManagerWrapper).notify(any(), any())
    }

    @Test
    fun `given grouping notification when send start collecting notification then create notification and notify manager`() {
        // given
        whenever(notificationProvider.createSummaryNotification()).doReturn(builder)

        // when
        tested.sendStartCollectingNotification(notificationId)

        // then
        verify(notificationProvider).createSummaryNotification()
        verify(builder).build()
        verify(notificationManagerWrapper).notify(any(), any())
    }

    @Test
    fun `given work in progress notification when work started then create work in progress notification and notify manager`() {
        // given
        whenever(notificationProvider.createWorkInProgressStartedNotification()).doReturn(builder)

        // when
        tested.sendWorkInProgressNotification()

        // then
        verify(notificationProvider).createWorkInProgressStartedNotification()
        verify(builder).build()
        verify(notificationManagerWrapper).notify(any(), any())
    }

    @Test
    fun `given work in progress notification when work finishes then cancel work in progress notification and send new one`() {
        // given
        whenever(notificationProvider.createWorkInProgressIsDoneNotification()).doReturn(builder)

        // then
        tested.cancelBackgroundWorkNotification()

        // then
        verify(notificationManagerWrapper).cancel(any())
        verify(builder).build()
        verify(notificationManagerWrapper).notify(any(), any())
    }

    @Test
    fun `given notification channel when during initialization then create channel`() {
        // given
        val notificationManagerWrapper: NotificationManagerWrapper = mock()
        val summaryChannel: NotificationChannel = mock()
        val savingChannel: NotificationChannel = mock()
        val workInProgressChannel: NotificationChannel = mock()

        // when
        NotificationUseCaseImpl(
            notificationManagerWrapper,
            summaryChannel,
            savingChannel,
            workInProgressChannel,
            mock()
        )

        // then
        verify(notificationManagerWrapper).createNotificationChannel(summaryChannel)
        verify(notificationManagerWrapper).createNotificationChannel(savingChannel)
        verify(notificationManagerWrapper).createNotificationChannel(workInProgressChannel)
    }
}