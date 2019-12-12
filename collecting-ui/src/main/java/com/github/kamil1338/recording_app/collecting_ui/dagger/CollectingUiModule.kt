package com.github.kamil1338.recording_app.collecting_ui.dagger

import androidx.lifecycle.ViewModel
import com.github.kamil1338.recording_app.collecting_ui.collecting.CollectingViewModel
import com.github.kamil1338.recording_app.collecting_ui.configure.ConfigCollectingFragment
import com.github.kamil1338.recording_app.collecting_ui.configure.ConfigCollectingViewModel
import com.github.kamil1338.recording_app.collecting_ui.get_started.CollectingGetStartedFragment
import com.github.kamil1338.recording_app.collecting_ui.get_started.CollectingGetStartedViewModel
import com.github.kamil1338.recording_app.collecting_use_case.dagger.SensorProviderModule
import com.github.kamil1338.recording_app.core_ui.ViewModelBuilder
import com.github.kamil1338.recording_app.core_ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CollectingUiModule {

    @Binds
    @IntoMap
    @ViewModelKey(CollectingViewModel::class)
    abstract fun bindCollectingViewModel(viewmodel: CollectingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CollectingGetStartedViewModel::class)
    abstract fun bindCollectingGetStartedViewModel(viewModel: CollectingGetStartedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfigCollectingViewModel::class)
    abstract fun bindConfigCollectingViewModel(viewModel: ConfigCollectingViewModel): ViewModel

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            CollectingGetStartedFragmentModule::class
        ]
    )
    abstract fun contributeCollectingGetStartedFragment(): CollectingGetStartedFragment

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            ConfigCollectingFragmentModule::class
        ]
    )
    abstract fun contributePrepareCollectingFragment(): ConfigCollectingFragment
}