package com.github.kamil1338.recording_app.collecting_use_case

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.github.kamil1338.activity_recognition_core.core.SensorData
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

open class SensorUseCaseImpl(
    private val sensorManager: SensorManager,
    private val sensor: Sensor
) : SensorUseCase {

    override fun execute(): Observable<SensorData> {
        var listener = SensorListener()
        return Observable.create<SensorData> { emitter ->
            listener = SensorListener().also {
                it.emitter = emitter
                sensorManager.registerListener(it, sensor, SensorManager.SENSOR_DELAY_FASTEST)
            }
        }
            .doOnDispose {
                stop(listener)
            }
    }

    private fun stop(listener: SensorListener) {
        sensorManager.unregisterListener(listener, sensor)
        listener.emitter = null
    }
}

internal class SensorListener : SensorEventListener {

    var emitter: ObservableEmitter<SensorData>? = null

    override fun onAccuracyChanged(event: Sensor?, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val sensorData = createSensorData(event)
            emitter?.onNext(sensorData)
        }
    }

    private fun createSensorData(event: SensorEvent): SensorData {
        return SensorData(
            event.values[0],
            event.values[1],
            event.values[2]
        )
    }
}