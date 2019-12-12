package com.github.kamil1338.recording_app.power_usecase

import android.os.PowerManager
import io.reactivex.Observable

class PowerUseCaseImpl(
    private val powerManager: PowerManager
) : PowerUseCase {

    override fun execute(): Observable<Unit> {
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG)
        return Observable.create<Unit> { emitter ->
            wakeLock.acquire()
            emitter.onNext(Unit)
        }.doOnDispose {
            wakeLock.release()
        }
    }

    companion object {
        const val WAKE_LOCK_TAG = "ActivityRecognition:Power"
    }
}