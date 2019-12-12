package com.github.kamil1338.recording_app.collecting_use_case.dagger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.github.kamil1338.recording_app.collecting_use_case.NotificationManagerWrapper
import com.github.kamil1338.recording_app.collecting_use_case.NotificationProvider
import com.github.kamil1338.recording_app.collecting_use_case.NotificationUseCase
import com.github.kamil1338.recording_app.collecting_use_case.NotificationUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationProviderModule {

    @Provides
    @Singleton
    fun provideNotificationManagerCompat(applicationContext: Context): NotificationManagerCompat =
        NotificationManagerCompat.from(applicationContext)

    @Provides
    @Singleton
    fun provideNotificationManagerWrapper(notificationManagerCompat: NotificationManagerCompat):
            NotificationManagerWrapper = NotificationManagerWrapper(notificationManagerCompat)

    @Provides
    @Singleton
    fun provideNotificationChannel(channelIds: ChannelIds): NotificationChannels {
        val summaryNotificationChannel = NotificationChannel(
            channelIds.summaryChannelId,
            "Start new collecting",
            NotificationManager.IMPORTANCE_HIGH
        )
        val savingNotificationChannel = NotificationChannel(
            channelIds.savingChannelId,
            "Batch collected",
            NotificationManager.IMPORTANCE_HIGH
        )
        val workInProgressNotificationChannel = NotificationChannel(
            channelIds.workInProgressChannelId,
            "Work in progress",
            NotificationManager.IMPORTANCE_HIGH
        )
        return NotificationChannels(
            summaryNotificationChannel,
            savingNotificationChannel,
            workInProgressNotificationChannel
        )
    }

    data class NotificationChannels(
        val summaryNotificationChannel: NotificationChannel,
        val savingNotificationChannel: NotificationChannel,
        val workInProgressNotificationChannel: NotificationChannel
    )

    @Provides
    @Singleton
    fun provideChannelIds(): ChannelIds = ChannelIds(
        "recording_app_starting_collecting_channel",
        "recording_app_successful_saving_channel",
        "recording_app_work_in_progress_channel"
    )

    data class ChannelIds(
        val summaryChannelId: String,
        val savingChannelId: String,
        val workInProgressChannelId: String
    )

    @Provides
    @Singleton
    fun provideNotificationProvider(
        applicationContext: Context,
        channelIds: ChannelIds
    ): NotificationProvider = NotificationProvider(
        applicationContext,
        channelIds.summaryChannelId,
        channelIds.savingChannelId,
        channelIds.workInProgressChannelId
    )

    @Provides
    @Singleton
    fun provideNotificationUseCase(
        notificationManagerWrapper: NotificationManagerWrapper,
        notificationChannels: NotificationChannels,
        notificationProvider: NotificationProvider
    ): NotificationUseCase =
        NotificationUseCaseImpl(
            notificationManagerWrapper,
            notificationChannels.summaryNotificationChannel,
            notificationChannels.savingNotificationChannel,
            notificationChannels.workInProgressNotificationChannel,
            notificationProvider
        )
}