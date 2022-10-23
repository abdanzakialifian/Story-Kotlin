package com.abdanzakialifian.storyapp.presentation.home.view

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentHomeBinding
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.home.adapter.HomePagingAdapter
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

    override fun initView() {
        setListStory()
        setUserSession()
        binding.imgLogout.setOnClickListener {
            setAlertDialog()
        }

        binding.fabAdd.setOnClickListener {
            val actionToCameraFragment = HomeFragmentDirections.actionHomeFragmentToCameraFragment()
            findNavController().navigate(actionToCameraFragment)
        }

        // give item click
        homePagingAdapter.setOnItemClickCallback(object : HomePagingAdapter.OnItemClickCallback {
            override fun onItemClicked(item: ListStory?, imageView: ImageView, textView: TextView) {
                // shared element in navigation component
                val extras = FragmentNavigatorExtras(
                    Pair(imageView, "detail_image"),
                    Pair(textView, "detail_name")
                )
                val actionToDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(item as ListStory)
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
        binding.apply {
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
                                        rvStory.adapter = homePagingAdapter
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

    private fun setAlertDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog_logout, null)
        val btnYes = view.findViewById<Button>(R.id.btn_yes)
        val btnNo = view.findViewById<Button>(R.id.btn_no)
        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        btnNo.setOnClickListener {
            builder.dismiss()
        }
        btnYes.setOnClickListener {
            val actionToLoginFragment = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(actionToLoginFragment)
            lifecycleScope.launchWhenStarted {
                viewModel.deleteDataStore()
            }
            builder.dismiss()
        }
        builder.show()
    }

    override fun onRefresh() {
        binding.apply {
            swipeRefresh.isRefreshing = false
            setListStory()
        }
    }
}