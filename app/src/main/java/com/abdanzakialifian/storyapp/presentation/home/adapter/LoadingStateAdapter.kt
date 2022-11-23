package com.abdanzakialifian.storyapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abdanzakialifian.storyapp.databinding.ItemLoadingPagingBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    inner class LoadingStateViewHolder(
        private val binding: ItemLoadingPagingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.btnRetry.setOnClickListener { retry.invoke() }
            }
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder =
        ItemLoadingPagingBinding.inflate(LayoutInflater.from(parent.context), parent, false).run {
            LoadingStateViewHolder(this)
        }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}