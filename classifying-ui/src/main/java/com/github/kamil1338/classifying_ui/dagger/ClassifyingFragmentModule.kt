package com.github.kamil1338.classifying_ui.dagger

import androidx.lifecycle.LifecycleOwner
import com.github.kamil1338.classifying_ui.ClassifyingFragment
import dagger.Module
import dagger.Provides

@Module
class ClassifyingFragmentModule {

    @Provides
    internal fun provideLifecycleOwner(fragment: ClassifyingFragment): LifecycleOwner =
        fragment
}