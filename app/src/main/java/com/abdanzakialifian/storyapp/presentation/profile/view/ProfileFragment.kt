package com.abdanzakialifian.storyapp.presentation.profile.view

import android.graphics.Color
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentProfileBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class ProfileFragment : BaseVBFragment<FragmentProfileBinding>() {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    override fun setupView() {

        lifecycleScope.launchWhenStarted {
            viewModel.languageCode
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { data ->
                    checkLocaleLanguage(data)
                }
        }

        setBackgroundProfile()

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.userName
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { name ->
                    binding.tvUserName.text = name
                    val splitName = name.split(" ")
                    val firstInitial = splitName.firstOrNull()
                    val lastInitial = splitName.lastOrNull()
                    val firstCharacter = firstInitial?.take(1)
                    val lastCharacter = lastInitial?.take(1)
                    binding.tvInitialName.text =
                        StringBuilder().append(firstCharacter).append(lastCharacter)
                }
        }
        binding.btnLogOut.setOnClickListener {
            setAlertDialog()
        }
        binding.tvEn.setOnClickListener {
            setLocaleLanguage(EN)
            viewModel.saveLanguageCode(EN)
        }
        binding.tvId.setOnClickListener {
            setLocaleLanguage(ID)
            viewModel.saveLanguageCode(ID)
        }
    }

    private fun checkLocaleLanguage(languageCode: String) {
        when (languageCode) {
            EN -> {
                binding.apply {
                    tvEn.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                    tvEn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
            ID -> {
                binding.apply {
                    tvId.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                    tvId.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
            else -> {
                binding.apply {
                    tvEn.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                    tvEn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
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
        if (languageCode == EN) {
            binding.apply {
                tvEn.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                tvEn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvId.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)
                tvId.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }
        } else {
            binding.apply {
                tvId.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                tvId.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                tvEn.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)
                tvEn.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }
        }
        restartApp()
    }

    private fun setBackgroundProfile() {
        val colors = arrayOf(
            Color.parseColor("#C0392B"),
            Color.parseColor("#2980B9"),
            Color.parseColor("#1ABC9C"),
            Color.parseColor("#F1C40F"),
            Color.parseColor("#95A5A6"),
            Color.parseColor("#34495E")
        )
        val randomColor = colors.random()

        binding.tvInitialName.background.setTint(randomColor)
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
            val actionToLoginFragment =
                ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            findNavController().navigate(actionToLoginFragment)
            lifecycleScope.launchWhenStarted {
                viewModel.deleteDataStore()
            }
            builder.dismiss()
        }
        builder.show()
    }

    private fun restartApp() {
        val actionToHomeFragment = ProfileFragmentDirections.actionProfileFragmentToHomeFragment()
        findNavController().navigate(actionToHomeFragment)
    }

    companion object {
        private const val EN = "en"
        private const val ID = "id"
    }
}