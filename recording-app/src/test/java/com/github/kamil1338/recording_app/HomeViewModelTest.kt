package com.github.kamil1338.recording_app

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.kamil1338.recording_app.storage_use_case.ExtractDatabaseUseCase
import com.github.kamil1338.activity_recognition_core.core.permission.CheckPermissionUseCase
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule()

    val checkPermissionUseCase: CheckPermissionUseCase = mock()
    val extractDatabaseUseCase: ExtractDatabaseUseCase = mock()

    val tested = HomeViewModel(checkPermissionUseCase, extractDatabaseUseCase)

    @Test
    fun `given check permission when permission is granted then emits positive event`() {
        // given
        whenever(checkPermissionUseCase.execute(any())).doReturn(Single.just(true))

        val observer: Observer<Boolean?> = mock()
        tested.permissionGranted.observeForever(observer)

        // when
        tested.checkWritePermission()

        // then
        verify(observer).onChanged(true)
        verify(observer).onChanged(null)
    }

    @Test
    fun `given check permission when permission is not grented then emits negative event`() {
        // when
        whenever(checkPermissionUseCase.execute(any())).doReturn(Single.just(false))

        val observer: Observer<Boolean?> = mock()
        tested.permissionGranted.observeForever(observer)

        // when
        tested.checkWritePermission()

        // then
        verify(observer).onChanged(false)
        verify(observer).onChanged(null)
    }

    @Test
    fun `given extracting database when finishes then emits positive event`() {
        // given
        whenever(extractDatabaseUseCase.execute()).doReturn(Completable.complete())

        val observer: Observer<Boolean?> = mock()
        tested.extractingSucceeded.observeForever(observer)

        // when
        tested.extractDatabase()

        // then
        verify(observer).onChanged(true)
        verify(observer).onChanged(null)
    }

    @Test
    fun `given extracting database when failed then emits negative event`() {
        // given
        whenever(extractDatabaseUseCase.execute()).doReturn(Completable.error(IOException()))

        val observer: Observer<Boolean?> = mock()
        tested.extractingSucceeded.observeForever(observer)

        // when
        tested.extractDatabase()

        // then
        verify(observer).onChanged(false)
        verify(observer).onChanged(null)
    }
}