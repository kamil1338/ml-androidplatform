package com.github.kamil1338.classifying_app.dagger

import com.github.kamil1338.classifying_app.ClassifyingActivity
import com.github.kamil1338.recording_app.core_ui.ViewModelBuilder
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClassifyingAppModule {

    @ContributesAndroidInjector (
        modules = [
            ViewModelBuilder::class
        ]
    )
    abstract fun contributeClassifyingActivity(): ClassifyingActivity
}