package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.data.repository.PostRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EmptyViewModelTest {

    @Mock
    private lateinit var postRepository: PostRepository

    private lateinit var emptyViewModel: EmptyViewModel

    @Before
    fun setUp() {
        emptyViewModel = EmptyViewModel(postRepository)
    }

    @Test
    fun givenFailedSetup_whenRetryTriggered_shouldInitiateDataRefresh() {
        emptyViewModel.onRetryClicked()
        verify(postRepository).retryFetchingData()
    }

    @Test
    fun givenFailedSetup_whenRetryTriggered_shouldInitiateDataRefreshOnce() {
        emptyViewModel.onRetryClicked()
        verify(postRepository, times(1)).retryFetchingData()
    }
}