package com.github.kamil1338.recording_app.storage_use_case.dagger

import android.content.Context
import androidx.room.Room
import com.github.kamil1338.recording_app.storage_use_case.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageProviderModule {

    @Singleton
    @Provides
    fun provideDatabaseFileName(): DatabaseFileName =
        DatabaseFileName("Recordings", ".db")

    data class DatabaseFileName(val filename: String, val type: String) {

        override fun toString() = "$filename$type"
    }

    @Singleton
    @Provides
    fun provideDataBase(
        applicationContext: Context,
        databaseFileName: DatabaseFileName
    ): RecordingDatabase = Room.databaseBuilder(
        applicationContext,
        RecordingDatabase::class.java,
        databaseFileName.toString()
    ).build()

    @Provides
    @Singleton
    fun provideSavingUseCase(database: RecordingDatabase): SavingUseCase =
        SavingUseCaseImpl(database.accelerometerDao())

    @Provides
    @Singleton
    fun provideDataCountFetchUseCase(database: RecordingDatabase): DataCountUseCase =
        DataCountFetchUseCaseImpl(database.accelerometerDao())

    @Provides
    @Singleton
    fun provideExtractDatabaseUseCase(
        applicationContext: Context,
        recordingDatabase: RecordingDatabase,
        databaseFileName: DatabaseFileName
    ): ExtractDatabaseUseCase =
        ExtractDatabaseUseCaseImpl(
            applicationContext,
            recordingDatabase,
            Pair(databaseFileName.filename, databaseFileName.type)
        )
}