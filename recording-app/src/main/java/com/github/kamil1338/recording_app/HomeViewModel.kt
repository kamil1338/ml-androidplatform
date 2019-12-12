package com.github.kamil1338.recording_app

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kamil1338.activity_recognition_core.core.permission.CheckPermissionUseCase
import com.github.kamil1338.recording_app.storage_use_case.ExtractDatabaseUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    private val checkPermissionUseCase: CheckPermissionUseCase,
    private val extractDatabaseUseCase: ExtractDatabaseUseCase
) : ViewModel() {

    val permissionGranted = MutableLiveData<Boolean?>()
    val extractingSucceeded = MutableLiveData<Boolean?>()

    val requestedPermission: String
        get() = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun checkWritePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        disposables += checkPermissionUseCase.execute(permission)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handlePermissionCheck) {}
    }

    private fun handlePermissionCheck(result: Boolean) {
        permissionGranted.value = result
        permissionGranted.value = null
    }

    fun extractDatabase() {
        disposables += extractDatabaseUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                ::handleExtractionSuccessfulResult,
                ::handleExtractionFailedResult
            )
    }

    private fun handleExtractionSuccessfulResult() {
        extractingSucceeded.value = true
        extractingSucceeded.value = null
    }

    private fun handleExtractionFailedResult(exception: Throwable) {
        extractingSucceeded.value = false
        extractingSucceeded.value = null
    }
}