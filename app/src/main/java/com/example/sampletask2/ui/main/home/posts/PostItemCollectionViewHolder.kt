package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.MyApp
import com.example.sampletask2.data.local.db.entity.CollectionEntity
import com.example.sampletask2.databinding.ItemViewPostCollectionBinding
import com.example.sampletask2.di.component.DaggerViewHolderComponent
import com.example.sampletask2.di.module.ViewHolderModule
import com.example.sampletask2.ui.base.BaseViewHolder
import javax.inject.Inject

class PostItemCollectionViewHolder(private val binding: ItemViewPostCollectionBinding) : BaseViewHolder(binding) {

    @Inject
    lateinit var viewModel: PostItemCollectionViewModel

    init {
        DaggerViewHolderComponent.builder()
            .applicationComponent(
                (binding.root.context.applicationContext as MyApp).applicationComponent
            )
            .viewHolderModule(ViewHolderModule(this))
            .build()
            .inject(this)
    }


    override fun onBind(any: Any) {
        (any as CollectionEntity).run {
            viewModel.updateData(this)
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }
}