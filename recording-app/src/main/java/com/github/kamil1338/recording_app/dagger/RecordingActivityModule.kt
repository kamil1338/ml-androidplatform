package com.github.kamil1338.recording_app.dagger

import androidx.fragment.app.FragmentManager
import com.github.kamil1338.activity_recognition_core.core.dagger.ActivityScope
import com.github.kamil1338.activity_recognition_core.dagger.FragmentScope
import com.github.kamil1338.recording_app.collecting_ui.configure.ConfigCollectingFragment
import com.github.kamil1338.recording_app.RecordingActivity
import com.github.kamil1338.recording_app.collecting_ui.dagger.ConfigCollectingFragmentModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class RecordingActivityModule {



    @FragmentScope
    @ContributesAndroidInjector(modules = [ConfigCollectingFragmentModule::class])
    abstract fun contributePrepareCollectingFragment(): ConfigCollectingFragment
}

@Module
internal class RecordingActivityProviderModule {

    @Provides
    @ActivityScope
    fun provideFragmentManager(activity: RecordingActivity): FragmentManager =
        activity.supportFragmentManager
}