package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.di.ViewModelScope
import javax.inject.Inject

@ViewModelScope
class EmptyViewModel @Inject constructor(
    private val postRepository: PostRepository
) {

    fun onRetryClicked() {
        postRepository.retryFetchingData()
    }
}