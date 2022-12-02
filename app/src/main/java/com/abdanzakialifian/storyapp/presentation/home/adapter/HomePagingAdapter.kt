package com.abdanzakialifian.storyapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdanzakialifian.storyapp.databinding.ItemListStoryBinding
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.utils.loadImageUrl
import javax.inject.Inject

class HomePagingAdapter @Inject constructor() :
    PagingDataAdapter<Stories, HomePagingAdapter.HomePagingViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class HomePagingViewHolder(private val binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Stories?) {
            binding.apply {
                imgUser.loadImageUrl(item?.photoUrl ?: "")
                imgUser.transitionName = item?.photoUrl
                tvName.text = item?.name
                tvName.transitionName = item?.name

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item, imgUser)
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
            item: Stories?,
            imageView: ImageView
        )
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Stories>() {
            override fun areItemsTheSame(
                oldItem: Stories,
                newItem: Stories
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Stories,
                newItem: Stories
            ): Boolean =
                oldItem == newItem
        }
    }
}