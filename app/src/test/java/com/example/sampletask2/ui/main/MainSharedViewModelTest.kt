package com.example.sampletask2.ui.main

import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.rx.TestSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainSharedViewModelTest {
    @Mock
    private lateinit var postRepository: PostRepository

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var testScheduler: TestScheduler

    private lateinit var viewModel: MainSharedViewModel

    @Before
    fun setUp() {
        compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val schedulerProvider = TestSchedulerProvider(testScheduler)
        viewModel = MainSharedViewModel(schedulerProvider, compositeDisposable, postRepository)
    }

    @Test
    fun givenFailedSetup_whenRetryTriggered_shouldInitiateDataRefresh() {
        viewModel.onRetryClicked()
        verify(postRepository).retryFetchingData()
    }

    @Test
    fun givenFailedSetup_whenRetryTriggered_shouldInitiateDataRefreshOne() {
        viewModel.onRetryClicked()
        verify(postRepository, times(1)).retryFetchingData()
    }
}