package com.example.sampletask2.ui.main.home.posts

import androidx.databinding.ObservableField
import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.data.repository.PostRepository
import com.example.sampletask2.di.ViewModelScope
import com.example.sampletask2.utils.network.NetworkHelper
import javax.inject.Inject

@ViewModelScope
class PostItemStoryViewModel @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val postRepository: PostRepository
) {

    private var storyEntity: StoryEntity? = null
    lateinit var messageIdListener: (Int) -> Unit
    lateinit var messageListener: (String) -> Unit

    val heroUrl: ObservableField<String> = ObservableField()
    val headline: ObservableField<String> = ObservableField()
    val summary: ObservableField<String> = ObservableField()
    val isPremium: ObservableField<Boolean> = ObservableField()


    fun updateData(storyEntity: StoryEntity) {
        this.storyEntity = storyEntity
        heroUrl.set(storyEntity.heroUrl)
        headline.set(storyEntity.headline)
        summary.set(storyEntity.summary)
        isPremium.set(storyEntity.isPremium)
    }

    fun onStoryClicked() {
        postRepository.updateSelectedStory(storyEntity)
    }
}