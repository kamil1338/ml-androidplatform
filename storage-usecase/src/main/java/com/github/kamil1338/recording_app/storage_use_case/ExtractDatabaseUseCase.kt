package com.github.kamil1338.recording_app.storage_use_case

import io.reactivex.Completable

interface ExtractDatabaseUseCase {

    fun execute(): Completable
}