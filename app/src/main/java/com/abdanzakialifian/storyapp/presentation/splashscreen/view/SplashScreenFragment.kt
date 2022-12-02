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
import java.util.*

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
@AndroidEntryPoint
class SplashScreenFragment : BaseVBFragment<FragmentSplashScreenBinding>() {

    private val viewModel by viewModels<SplashScreenViewModel>()

    override fun getViewBinding(): FragmentSplashScreenBinding =
        FragmentSplashScreenBinding.inflate(layoutInflater)

    override fun setupView() {
        viewModel.getUserSession()
        viewModel.getLanguageCode()

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launchWhenStarted {
                viewModel.getUserSession
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

        lifecycleScope.launchWhenStarted {
            viewModel.getLanguageCode
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { data ->
                    setLocaleLanguage(data)
                }
        }
    }

    private fun setLocaleLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resource = requireActivity().resources
        val config = resource.configuration
        config.setLocale(locale)
        resource.updateConfiguration(config, resource.displayMetrics)
    }

    companion object {
        private const val DELAY_SPLASH_SCREEN = 5000L
    }
}