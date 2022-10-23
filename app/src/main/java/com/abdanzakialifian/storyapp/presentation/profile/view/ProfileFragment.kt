package com.abdanzakialifian.storyapp.presentation.profile.view

import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentProfileBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.home.view.HomeFragmentDirections
import com.abdanzakialifian.storyapp.presentation.profile.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseVBFragment<FragmentProfileBinding>() {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)

    override fun initView() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        lifecycleScope.launchWhenStarted {
            viewModel.userName
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { name ->
                    binding.tvUserName.text = name
                }
        }
        binding.btnLogOut.setOnClickListener {
            setAlertDialog()
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
}