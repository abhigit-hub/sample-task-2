package com.example.sampletask2.ui.main.home.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sampletask2.data.local.db.entity.CollectionEntity
import com.example.sampletask2.data.local.db.entity.StoryEntity
import com.example.sampletask2.databinding.ItemViewEmptyBinding
import com.example.sampletask2.databinding.ItemViewPostCollectionBinding
import com.example.sampletask2.databinding.ItemViewPostStoryBinding
import com.example.sampletask2.ui.base.BaseViewHolder
import java.util.*

class PostAdapter(private val list: ArrayList<Any>) : RecyclerView.Adapter<BaseViewHolder>() {
    companion object {

        const val VIEW_TYPE_COLLECTION = 0
        const val VIEW_TYPE_STORY = 1
        const val VIEW_TYPE_EMPTY = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            VIEW_TYPE_STORY -> PostItemStoryViewHolder(
                ItemViewPostStoryBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            VIEW_TYPE_COLLECTION -> PostItemCollectionViewHolder(
                ItemViewPostCollectionBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> EmptyViewHolder(
                ItemViewEmptyBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }


    fun appendData(list: List<Any>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =
        when (list.size) {
            0 -> 1
            else -> list.size
        }

    override fun getItemViewType(position: Int): Int =
        when (list.size) {
            0 -> VIEW_TYPE_EMPTY
            else -> when (list[position]) {
                is CollectionEntity -> VIEW_TYPE_COLLECTION
                is StoryEntity -> VIEW_TYPE_STORY
                else -> VIEW_TYPE_EMPTY
            }
        }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (list.size) {
            0 -> holder.onBind(Any())
            else -> holder.onBind(list[position])
        }
    }
}
