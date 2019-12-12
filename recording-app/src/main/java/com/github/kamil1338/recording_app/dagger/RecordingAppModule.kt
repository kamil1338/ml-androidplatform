package com.github.kamil1338.recording_app.dagger

import androidx.lifecycle.ViewModel
import com.github.kamil1338.activity_recognition_core.core.dagger.CoreProviderModule
import com.github.kamil1338.recording_app.HomeViewModel
import com.github.kamil1338.recording_app.RecordingActivity
import com.github.kamil1338.recording_app.core_ui.ViewModelBuilder
import com.github.kamil1338.recording_app.core_ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(includes = [CoreProviderModule::class])
abstract class RecordingAppModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindCollectingViewModel(viewModel: HomeViewModel): ViewModel

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    abstract fun contributeRecordingActivity(): RecordingActivity
}