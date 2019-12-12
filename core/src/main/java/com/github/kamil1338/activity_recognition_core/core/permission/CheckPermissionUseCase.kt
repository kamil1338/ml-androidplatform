package com.github.kamil1338.activity_recognition_core.core.permission

import io.reactivex.Single

interface CheckPermissionUseCase {

    fun execute(permission: String): Single<Boolean>
}