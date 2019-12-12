package com.github.kamil1338.recording_app.power_usecase

import io.reactivex.Observable

interface PowerUseCase {

    fun execute(): Observable<Unit>
}