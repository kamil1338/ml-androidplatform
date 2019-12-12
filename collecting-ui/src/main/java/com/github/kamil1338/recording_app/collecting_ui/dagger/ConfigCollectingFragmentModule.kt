package com.github.kamil1338.recording_app.collecting_ui.dagger

import androidx.lifecycle.LifecycleOwner
import com.github.kamil1338.recording_app.collecting_ui.configure.ConfigCollectingFragment
import dagger.Module
import dagger.Provides

@Module
class ConfigCollectingFragmentModule {

    @Provides
    internal fun provideLifeCycleOwner(fragment: ConfigCollectingFragment): LifecycleOwner
            = fragment
}