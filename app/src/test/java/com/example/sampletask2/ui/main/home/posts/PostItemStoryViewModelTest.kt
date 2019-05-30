package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.utils.network.NetworkHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostItemStoryViewModelTest {

    @Mock
    private lateinit var postRepository: PostRepository

    @Mock
    private lateinit var networkHelper: NetworkHelper

    private var storyEntity: StoryEntity? = null

    private lateinit var viewModel: PostItemStoryViewModel

    @Before
    fun setUp() {
        viewModel = PostItemStoryViewModel(networkHelper, postRepository)
    }

    @Test
    fun givenDataSetUp_whenStoryClicked_shouldUpdateSelectedStory() {
        viewModel.onStoryClicked()
        Mockito.verify(postRepository).updateSelectedStory(storyEntity)
    }
}