package com.abdanzakialifian.storyapp.presentation.detail.view

import android.graphics.drawable.Drawable
import android.transition.TransitionInflater
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentDetailBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseVBFragment<FragmentDetailBinding>() {

    private val args by navArgs<DetailFragmentArgs>()

    override fun getViewBinding(): FragmentDetailBinding =
        FragmentDetailBinding.inflate(layoutInflater)

    override fun setupView() {
        binding.apply {
            imgBack.setOnClickListener {
                findNavController().navigateUp()
            }
            setSharedElementTransitionOnEnter()
            postponeEnterTransition()

            tvNameUser.text = args.name
            tvDescriptionStory.text = args.description
            imgUser.apply {
                imgUser.transitionName = args.imageUrl
                startEnterTransitionAfterLoadingImage(args.imageUrl, this)
            }
        }
    }

    private fun startEnterTransitionAfterLoadingImage(
        imageAddress: String,
        imageView: ImageView
    ) {
        Glide.with(this)
            .load(imageAddress)
            .dontAnimate()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
            .into(imageView)
    }

    private fun setSharedElementTransitionOnEnter() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_element_transition)
    }
}