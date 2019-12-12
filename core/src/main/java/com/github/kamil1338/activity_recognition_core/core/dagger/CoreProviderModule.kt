package com.github.kamil1338.activity_recognition_core.core.dagger

import android.content.Context
import com.github.kamil1338.activity_recognition_core.core.permission.CheckPermissionUseCase
import com.github.kamil1338.activity_recognition_core.core.permission.CheckPermissionUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreProviderModule {

    @Provides
    @Singleton
    fun provideCheckPermissionUseCase(applicationContext: Context): CheckPermissionUseCase =
        CheckPermissionUseCaseImpl(applicationContext)
}