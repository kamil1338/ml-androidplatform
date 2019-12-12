package com.github.kamil1338.classifying_usecase.classifying

import com.github.kamil1338.activity_recognition_core.core.ActivityType
import io.reactivex.Observable

interface ClassifyingUseCase {

    fun execute(data: List<ClassificationData>): Observable<ActivityType>

    companion object {
        const val BYTE_SIZE_OF_FLOAT = 4
        const val INPUT_BATCH_SIZE = 200
        const val OUTPUT_LENGTH = 3
        const val RESULTS_TO_SHOW = 1
    }
}

data class ClassificationData(val x: Float, val y: Float, val z: Float)