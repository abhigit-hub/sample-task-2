package com.example.sampletask2.ui.main.home.posts

import com.example.sampletask2.MyApp
import com.example.sampletask2.databinding.ItemViewEmptyBinding
import com.example.sampletask2.di.component.DaggerViewHolderComponent
import com.example.sampletask2.di.module.ViewHolderModule
import com.example.sampletask2.ui.base.BaseViewHolder
import javax.inject.Inject

class EmptyViewHolder(private val binding: ItemViewEmptyBinding) : BaseViewHolder(binding) {

    @Inject
    lateinit var viewModel: EmptyViewModel

    init {
        DaggerViewHolderComponent.builder()
            .applicationComponent((binding.root.context.applicationContext as MyApp).applicationComponent)
            .viewHolderModule(ViewHolderModule(this))
            .build()
            .inject(this)
    }

    override fun onBind(any: Any) {
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }
}