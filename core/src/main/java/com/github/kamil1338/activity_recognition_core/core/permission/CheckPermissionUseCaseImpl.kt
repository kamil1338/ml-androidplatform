package com.github.kamil1338.activity_recognition_core.core.permission

import android.content.Context
import android.content.pm.PackageManager
import io.reactivex.Single

class CheckPermissionUseCaseImpl(private val applicationContext: Context) : CheckPermissionUseCase {

    override fun execute(permission: String) =
        Single.create<Boolean> { emitter ->
            val value = when (applicationContext.checkSelfPermission(permission)) {
                PackageManager.PERMISSION_GRANTED -> true
                PackageManager.PERMISSION_DENIED -> false
                else -> false
            }
            emitter.onSuccess(value)
        }
}