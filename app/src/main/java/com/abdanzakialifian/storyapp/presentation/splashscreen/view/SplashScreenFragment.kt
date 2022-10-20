package com.abdanzakialifian.storyapp.presentation.splashscreen.view

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.databinding.FragmentSplashScreenBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.splashscreen.viewmodel.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : BaseVBFragment<FragmentSplashScreenBinding>() {

    private val viewModel by viewModels<SplashScreenViewModel>()

    override fun getViewBinding(): FragmentSplashScreenBinding =
        FragmentSplashScreenBinding.inflate(layoutInflater)

    override fun initView() {
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launchWhenStarted {
                viewModel.getUserSession()
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { isLogin ->
                        if (isLogin) {
                            val actionToHomeFragment =
                                SplashScreenFragmentDirections.actionSplashScreenFragmentToHomeFragment()
                            findNavController().navigate(actionToHomeFragment)
                        } else {
                            val actionToLoginFragment =
                                SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                            findNavController().navigate(actionToLoginFragment)
                        }
                    }
            }
        }, DELAY_SPLASH_SCREEN)
    }

    companion object {
        private const val DELAY_SPLASH_SCREEN = 5000L
    }
}