package com.abdanzakialifian.storyapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdanzakialifian.storyapp.databinding.ItemListStoryBinding
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.utils.loadImageUrl
import javax.inject.Inject

class HomePagingAdapter @Inject constructor() :
    PagingDataAdapter<ListStory, HomePagingAdapter.HomePagingViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class HomePagingViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStory?) {
            binding.apply {
                imgUser.loadImageUrl(item?.photoUrl ?: "")
                tvName.text = item?.name

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item, imgUser, tvName)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HomePagingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePagingViewHolder =
        ItemListStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).run {
            HomePagingViewHolder(this)
        }

    interface OnItemClickCallback {
        fun onItemClicked(
            item: ListStory?,
            imageView: ImageView,
            textView: TextView
        )
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean =
                oldItem == newItem
        }
    }
}