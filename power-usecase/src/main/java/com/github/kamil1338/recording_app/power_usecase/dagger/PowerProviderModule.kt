package com.github.kamil1338.recording_app.power_usecase.dagger

import android.content.Context
import android.os.PowerManager
import com.github.kamil1338.recording_app.power_usecase.PowerUseCase
import com.github.kamil1338.recording_app.power_usecase.PowerUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PowerProviderModule {

    @Singleton
    @Provides
    fun providePowerManager(applicationContext: Context): PowerManager =
        applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager

    @Singleton
    @Provides
    fun providePowerUseCase(powerManager: PowerManager): PowerUseCase =
        PowerUseCaseImpl(powerManager)
}