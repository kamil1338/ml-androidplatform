package com.github.kamil1338.recording_app.collecting_use_case.dagger

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCase
import com.github.kamil1338.recording_app.collecting_use_case.SensorUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SensorProviderModule {

    @Provides
    @Singleton
    fun provideSensorManager(applicationContext: Context): SensorManager =
        applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    fun provideSensor(sensorManager: SensorManager): Sensor =
        sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

    @Provides
    @Singleton
    fun provideSensorUseCase(sensorManager: SensorManager, sensor: Sensor): SensorUseCase =
        SensorUseCaseImpl(sensorManager, sensor)
}