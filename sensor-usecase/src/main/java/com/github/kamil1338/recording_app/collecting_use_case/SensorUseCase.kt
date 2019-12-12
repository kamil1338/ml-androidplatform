package com.github.kamil1338.recording_app.collecting_use_case

import com.github.kamil1338.activity_recognition_core.core.SensorData
import io.reactivex.Observable

interface SensorUseCase {

    fun execute(): Observable<SensorData>
}