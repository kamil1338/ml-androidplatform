package com.github.kamil1338.classifying_usecase.dagger

import android.content.Context
import android.content.res.AssetManager
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCase
import com.github.kamil1338.classifying_usecase.classifying.ClassifyingUseCaseImpl
import com.github.kamil1338.classifying_usecase.classifying.ResultCalculator
import com.github.kamil1338.classifying_usecase.sound.SoundUseCase
import com.github.kamil1338.classifying_usecase.sound.SoundUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ClassifyingProviderModule {

    @Provides
    @Singleton
    fun provideAssetManager(applicationContext: Context): AssetManager =
        applicationContext.assets

    class ModelsPath(
        val xModelPath: String,
        val yModelPath: String,
        val zModelPath: String,
        val labels: String
    )

    @Provides
    @Singleton
    fun provideModelsPath(): ModelsPath =
        ModelsPath(
            "activity_classification_x.tflite",
            "activity_classification_y.tflite",
            "activity_classification_z.tflite",
            "labels.txt"
        )

    @Provides
    @Singleton
    fun provideResultCalculator() = ResultCalculator()

    @Provides
    @Singleton
    fun provideClassifyingUseCase(
        assetManager: AssetManager,
        resultCalculator: ResultCalculator,
        modelsPath: ModelsPath
    ): ClassifyingUseCase =
        ClassifyingUseCaseImpl(
            assetManager,
            resultCalculator,
            modelsPath.xModelPath,
            modelsPath.yModelPath,
            modelsPath.zModelPath,
            modelsPath.labels
        )

    @Provides
    @Singleton
    fun provideSoundUseCase(applicationContext: Context): SoundUseCase =
        SoundUseCaseImpl(applicationContext)
}