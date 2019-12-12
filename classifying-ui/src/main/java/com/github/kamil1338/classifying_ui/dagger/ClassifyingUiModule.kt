package com.github.kamil1338.classifying_ui.dagger

import androidx.lifecycle.ViewModel
import com.github.kamil1338.classifying_ui.ClassifyingFragment
import com.github.kamil1338.classifying_ui.ClassifyingViewModel
import com.github.kamil1338.classifying_usecase.dagger.ClassifyingProviderModule
import com.github.kamil1338.recording_app.core_ui.ViewModelBuilder
import com.github.kamil1338.recording_app.core_ui.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ClassifyingUiModule {

    @Binds
    @IntoMap
    @ViewModelKey(ClassifyingViewModel::class)
    abstract fun bindClassifyingViewModel(viewModel: ClassifyingViewModel): ViewModel

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            ClassifyingFragmentModule::class
        ]
    )
    abstract fun contributeClassificationFragment(): ClassifyingFragment
}