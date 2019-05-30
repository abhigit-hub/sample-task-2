package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.data.local.db.entity.CollectionEntity
import com.example.sampletask2.data.repository.PostRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PostItemCollectionViewModelTest {

    private val collectionName = "Trending"

    @Mock
    private lateinit var postRepository: PostRepository

    private lateinit var collectionEntity: CollectionEntity

    private lateinit var viewModel: PostItemCollectionViewModel

    @Before
    fun setUp() {
        viewModel = PostItemCollectionViewModel(postRepository)
        collectionEntity = CollectionEntity(
            1, 1, collectionName,
            "https://www.google.com/", "Trending"
        )
    }

    @Test
    fun givenRVisUpAndRunning_whenUpdateDataIsTriggered_shouldUpdateTabName() {
        viewModel.updateData(collectionEntity)
        Mockito.verify(postRepository).updateTabName(collectionName)
    }

    @Test
    fun givenRVisUpAndRunning_whenUpdateDataIsTriggered_shouldUpdateTabNameOnce() {
        viewModel.updateData(collectionEntity)
        Mockito.verify(postRepository, Times(1)).updateTabName(collectionName)
    }
}