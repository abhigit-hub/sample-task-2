package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.MyApp
import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.databinding.ItemViewPostStoryBinding
import com.example.sampletask2.di.component.DaggerViewHolderComponent
import com.example.sampletask2.di.module.ViewHolderModule
import com.example.sampletask2.ui.base.BaseViewHolder
import com.example.sampletask2.utils.display.Toaster
import javax.inject.Inject

class PostItemStoryViewHolder(private val binding: ItemViewPostStoryBinding) : BaseViewHolder(binding) {

    @Inject
    lateinit var storyViewModel: PostItemStoryViewModel

    init {
        DaggerViewHolderComponent.builder()
            .applicationComponent(
                (binding.root.context.applicationContext as MyApp).applicationComponent
            )
            .viewHolderModule(ViewHolderModule(this))
            .build()
            .inject(this)

        storyViewModel.messageIdListener = {
            Toaster.show(
                binding.root.context,
                binding.root.context.getString(it)
            )
        }

        storyViewModel.messageListener = {
            Toaster.show(
                binding.root.context,
                it
            )
        }
    }

    override fun onBind(any: Any) {
        (any as StoryEntity).run {
            storyViewModel.updateData(this)
            binding.viewModel = storyViewModel
            binding.executePendingBindings()
        }
    }
}