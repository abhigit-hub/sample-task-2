package com.example.sampletask2.di.component

import com.example.sampletask2.di.ViewModelScope
import com.example.sampletask2.di.module.ViewHolderModule
import com.example.sampletask2.ui.main.home.posts.EmptyViewHolder
import com.example.sampletask2.ui.main.home.posts.PostItemCollectionViewHolder
import com.example.sampletask2.ui.main.home.posts.PostItemStoryViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {
    fun inject(holder: PostItemStoryViewHolder)

    fun inject(holder: PostItemCollectionViewHolder)

    fun inject(holder: EmptyViewHolder)
}