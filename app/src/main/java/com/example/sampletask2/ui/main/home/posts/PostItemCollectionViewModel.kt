package com.example.sampletask2.ui.main.home.posts

import androidx.databinding.ObservableField
import com.example.sampletask2.data.local.db.entity.CollectionEntity
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.di.ViewModelScope
import javax.inject.Inject

@ViewModelScope
class PostItemCollectionViewModel @Inject constructor(
    private val postRepository: PostRepository
) {

    private var collectionEntity: CollectionEntity? = null

    val collectionName: ObservableField<String> = ObservableField()

    fun updateData(collectionEntity: CollectionEntity) {
        this.collectionEntity = collectionEntity
        collectionName.set(collectionEntity.name)
        postRepository.updateTabName(collectionEntity.name)
    }
}