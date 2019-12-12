package com.github.kamil1338.activity_recognition_core.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

class CheckPermissionUseCaseTest {

    val applicationContext: Context = mock()

    val tested = CheckPermissionUseCaseImpl(applicationContext)

    @Test
    fun `given use case when permission is granted then emits positive value`() {
        // given
        val permissionToCheck = Manifest.permission.WRITE_EXTERNAL_STORAGE
        whenever(applicationContext.checkSelfPermission(any()))
            .doReturn(PackageManager.PERMISSION_GRANTED)

        // when
        val observable = tested.execute(permissionToCheck).test()

        // then
        observable.assertValue(true)
    }

    @Test
    fun `given use case when permission is not granted then emits negative value`() {
        // given
        val permissionToCheck = Manifest.permission.WRITE_EXTERNAL_STORAGE
        whenever(applicationContext.checkSelfPermission(any()))
            .doReturn(PackageManager.PERMISSION_DENIED)

        // when
        val observable = tested.execute(permissionToCheck).test()

        // then
        observable.assertValue(false)
    }
}