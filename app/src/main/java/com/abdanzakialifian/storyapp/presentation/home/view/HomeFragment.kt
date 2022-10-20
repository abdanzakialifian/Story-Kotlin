package com.abdanzakialifian.storyapp.presentation.home.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.databinding.FragmentHomeBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.home.adapter.HomePagingAdapter
import com.abdanzakialifian.storyapp.presentation.home.viewmodel.HomeViewModel
import com.abdanzakialifian.storyapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseVBFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var homePagingAdapter: HomePagingAdapter
    private val viewModel by viewModels<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun initView() {
        setListStory()
        setUserSession()
        setLogoutUser()
    }

    private fun setUserSession() {
        lifecycleScope.launchWhenStarted {
            viewModel.saveUserSession(true)
        }
    }

    private fun setLogoutUser() {
        binding.imgLogout.setOnClickListener {
            val actionToLoginFragment = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(actionToLoginFragment)
            lifecycleScope.launchWhenStarted {
                viewModel.deleteDataStore()
            }
        }
    }

    private fun setListStory() {
        binding.apply {
            rvStory.adapter = homePagingAdapter
            rvStory.setHasFixedSize(true)
            lifecycleScope.launchWhenStarted {
                viewModel.uiStateGetAllStories
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect {
                        when (it.status) {
                            Status.LOADING -> {}
                            Status.SUCCESS -> {
                                it.data?.let { pagingData ->
                                    homePagingAdapter.submitData(lifecycle, pagingData)
                                }
                            }
                            Status.ERROR -> {}
                        }
                    }
            }
        }
    }
}