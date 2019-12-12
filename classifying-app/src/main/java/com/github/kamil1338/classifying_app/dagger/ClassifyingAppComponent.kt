package com.github.kamil1338.classifying_app.dagger

import android.content.Context
import com.github.kamil1338.classifying_app.ActivityClassifyingApplication
import com.github.kamil1338.classifying_ui.dagger.ClassifyingUiModule
import com.github.kamil1338.classifying_usecase.dagger.ClassifyingModule
import com.github.kamil1338.recording_app.collecting_use_case.dagger.SensorModule
import com.github.kamil1338.recording_app.power_usecase.dagger.PowerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ClassifyingAppModule::class,
        ClassifyingUiModule::class,
        ClassifyingModule::class,
        SensorModule::class,
        PowerModule::class
    ]
)
interface ClassifyingAppComponent : AndroidInjector<ActivityClassifyingApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ClassifyingAppComponent
    }
}