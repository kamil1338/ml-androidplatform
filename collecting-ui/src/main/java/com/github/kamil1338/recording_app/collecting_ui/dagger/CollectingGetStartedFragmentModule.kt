package com.github.kamil1338.recording_app.collecting_ui.dagger

import androidx.lifecycle.LifecycleOwner
import com.github.kamil1338.recording_app.collecting_ui.get_started.CollectingGetStartedFragment
import dagger.Module
import dagger.Provides

@Module
class CollectingGetStartedFragmentModule {

    @Provides
    internal fun provideLifecycleOwner(fragment: CollectingGetStartedFragment): LifecycleOwner
            = fragment
}