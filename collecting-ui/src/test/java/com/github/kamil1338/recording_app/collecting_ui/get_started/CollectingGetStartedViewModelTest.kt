package com.github.kamil1338.recording_app.collecting_ui.get_started

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.kamil1338.activity_recognition_core.core.testing.ReactiveXUtils
import com.github.kamil1338.recording_app.storage_use_case.DataCountUseCase
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test

class CollectingGetStartedViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils().getTestRule()

    val dataCountFetchUseCase: DataCountUseCase = mock()

    @Test
    fun `given data live datas when started then updated`() {
        // given
        whenever(dataCountFetchUseCase.dataCount(any())).doReturn(Single.just(1L))
        whenever(dataCountFetchUseCase.recordingCount(any())).doReturn(Single.just(1L))
        val tested = CollectingGetStartedViewModel(dataCountFetchUseCase)

        // when
        val observerRunning: Observer<String> = mock()
        val observerWalking: Observer<String> = mock()
        val observerOther: Observer<String> = mock()
        tested.runningDataCount.observeForever(observerRunning)
        tested.walkingDataCount.observeForever(observerWalking)
        tested.otherDataCount.observeForever(observerOther)

        tested.onStart(mock())

        // then
        verify(observerRunning, times(3)).onChanged(any())
        verify(observerWalking, times(3)).onChanged(any())
        verify(observerOther, times(3)).onChanged(any())
    }

    @Test
    fun `given data when view model started then invoke`() {
        // given
        whenever(dataCountFetchUseCase.dataCount(any())).doReturn(Single.just(1L))
        whenever(dataCountFetchUseCase.recordingCount(any())).doReturn(Single.just(1L))
        val tested = CollectingGetStartedViewModel(dataCountFetchUseCase)

        // when
        tested.onStart(mock())

        // then
        verify(dataCountFetchUseCase, times(3)).dataCount(any())
    }

    @Test
    fun `given recording when view model started then invoke`() {
        // given
        whenever(dataCountFetchUseCase.dataCount(any())).doReturn(Single.just(1L))
        whenever(dataCountFetchUseCase.recordingCount(any())).doReturn(Single.just(1L))
        val tested = CollectingGetStartedViewModel(dataCountFetchUseCase)

        // when
        tested.onStart(mock())

        // then
        verify(dataCountFetchUseCase, times(3)).recordingCount(any())
    }
}