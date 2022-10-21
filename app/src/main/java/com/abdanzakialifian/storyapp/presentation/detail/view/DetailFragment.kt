package com.abdanzakialifian.storyapp.presentation.detail.view

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdanzakialifian.storyapp.databinding.FragmentDetailBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.utils.loadImageUrl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    private val args by navArgs<DetailFragmentArgs>()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun initView() {
        binding.apply {
            imgBack.setOnClickListener {
                findNavController().navigateUp()
            }
            imgUser.loadImageUrl(args.storie.photoUrl ?: "")
            tvNameUser.text = args.storie.name
            tvDescriptionStory.text = args.storie.description
        }
    }
}