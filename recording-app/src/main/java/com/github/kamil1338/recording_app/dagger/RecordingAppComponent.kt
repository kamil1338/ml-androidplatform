package com.github.kamil1338.recording_app.dagger

import android.content.Context
import com.github.kamil1338.recording_app.ActivityRecognitionApplication
import com.github.kamil1338.recording_app.collecting_ui.dagger.CollectingUiModule
import com.github.kamil1338.recording_app.collecting_use_case.dagger.NotificationModule
import com.github.kamil1338.recording_app.collecting_use_case.dagger.SensorModule
import com.github.kamil1338.recording_app.power_usecase.dagger.PowerModule
import com.github.kamil1338.recording_app.storage_use_case.dagger.StorageModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        RecordingAppModule::class,
        CollectingUiModule::class,
        SensorModule::class,
        NotificationModule::class,
        StorageModule::class,
        PowerModule::class
    ]
)
interface RecordingAppComponent : AndroidInjector<ActivityRecognitionApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): RecordingAppComponent
    }
}