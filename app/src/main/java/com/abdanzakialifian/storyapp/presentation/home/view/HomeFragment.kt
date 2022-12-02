package com.abdanzakialifian.storyapp.presentation.home.view

import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.abdanzakialifian.storyapp.databinding.FragmentHomeBinding
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.home.adapter.HomePagingAdapter
import com.abdanzakialifian.storyapp.presentation.home.adapter.LoadingStateAdapter
import com.abdanzakialifian.storyapp.presentation.home.viewmodel.HomeViewModel
import com.abdanzakialifian.storyapp.utils.Status
import com.abdanzakialifian.storyapp.utils.gone
import com.abdanzakialifian.storyapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeBinding>(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var homePagingAdapter: HomePagingAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun setupView() {
        setListStory()
        setUserSession()

        binding.imgProfile.setOnClickListener {
            val actionToProfileFragment =
                HomeFragmentDirections.actionHomeFragmentToProfileFragment()
            findNavController().navigate(actionToProfileFragment)
        }

        binding.imgMapMarker.setOnClickListener {
            val actionToMapsFragment = HomeFragmentDirections.actionHomeFragmentToMapsFragment()
            findNavController().navigate(actionToMapsFragment)
        }

        binding.fabAdd.setOnClickListener {
            val actionToCameraFragment = HomeFragmentDirections.actionHomeFragmentToCameraFragment()
            findNavController().navigate(actionToCameraFragment)
        }

        // give item click
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.OnItemClickCallback {
            override fun onItemClicked(item: Stories?, imageView: ImageView) {
                // shared element in navigation component
                val extras = FragmentNavigatorExtras(
                    Pair(imageView, item?.photoUrl ?: "")
                )
                val actionToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        item?.photoUrl ?: "",
                        item?.name ?: "",
                        item?.description ?: ""
                    )
                findNavController().navigate(actionToDetailFragment, extras)
            }
        })

        // handle physical back pressed
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAffinity()
                }
            })

        binding.swipeRefresh.setOnRefreshListener(this)
    }

    private fun setUserSession() {
        lifecycleScope.launchWhenStarted {
            viewModel.saveUserSession(true)
        }
    }

    private fun setListStory() {
        viewModel.getToken()

        lifecycleScope.launchWhenStarted {
            viewModel.getToken
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { token ->
                    viewModel.getAllStories(token)
                }
        }

        binding.apply {
            postponeEnterTransition()
            rvStory.viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
            lifecycleScope.launchWhenStarted {
                viewModel.uiStateGetAllStories
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        when (it.status) {
                            Status.LOADING -> {
                                binding.apply {
                                    shimmerLayout.visible()
                                    rvStory.gone()
                                    shimmerLayout.startShimmer()
                                }
                            }
                            Status.SUCCESS -> {
                                binding.apply {
                                    shimmerLayout.gone()
                                    shimmerLayout.stopShimmer()
                                    if (it.data != null) {
                                        rvStory.adapter = homePagingAdapter.withLoadStateFooter(
                                            footer = LoadingStateAdapter {
                                                homePagingAdapter.retry()
                                            }
                                        )
                                        rvStory.setHasFixedSize(true)
                                        rvStory.visible()
                                        homePagingAdapter.submitData(lifecycle, it.data)
                                    } else {
                                        rvStory.gone()
                                        emptyAnimation.visible()
                                    }
                                }
                            }
                            Status.ERROR -> {
                                binding.apply {
                                    shimmerLayout.gone()
                                    shimmerLayout.stopShimmer()
                                    rvStory.gone()
                                    errorAnimation.visible()
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onRefresh() {
        binding.apply {
            swipeRefresh.isRefreshing = false
            setListStory()
        }
    }
}