package com.abdanzakialifian.storyapp.presentation.login.view

import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentLoginBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.login.viewmodel.LoginViewModel
import com.abdanzakialifian.storyapp.utils.Status
import com.abdanzakialifian.storyapp.utils.gone
import com.abdanzakialifian.storyapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginFragment : BaseVBFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun setupView() {
        login()
        // navigate to registration page
        binding.tvRegister.setOnClickListener {
            val actionToRegistrationFragment =
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(actionToRegistrationFragment)
        }

        // handle physical back pressed
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finishAffinity()
                }
            })
    }

    private fun login() {
        binding.apply {
            // get edittext from text input layout
            val email = edtEmail.editText
            val password = edtPassword.editText
            btnSignIn.setOnClickListener {
                if (email?.text?.isEmpty() == true && password?.text?.isEmpty() == true) {
                    edtEmail.isErrorEnabled = !isValidateString(email.text.toString())
                    edtPassword.isErrorEnabled = password.text?.isEmpty() == true
                } else {
                    binding.layoutLoading.visible()
                    binding.btnSignIn.text = ""
                    binding.btnSignIn.isEnabled = false
                    setLogin(email, password)
                }
            }
        }
    }

    private fun setLogin(email: EditText?, password: EditText?) {
        val jsonObject = JSONObject().apply {
            put(EMAIL, email?.text?.toString()?.trim())
            put(PASSWORD, password?.text?.toString()?.trim())
        }.toString()

        val requestBody = jsonObject.toRequestBody("application/json".toMediaTypeOrNull())

        lifecycleScope.launchWhenStarted {
            viewModel.loginUser(requestBody)
            viewModel.uiStateLoginUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.layoutLoading.visible()
                            binding.btnSignIn.text = ""
                            binding.btnSignIn.isEnabled = false
                        }
                        Status.SUCCESS -> {
                            binding.layoutLoading.gone()
                            binding.btnSignIn.text =
                                requireContext().resources.getString(R.string.sign_in)
                            binding.btnSignIn.isEnabled = true
                            val token = it.data?.loginResult?.token
                            val name = it.data?.loginResult?.name
                            lifecycleScope.launchWhenStarted {
                                viewModel.saveUserData(token ?: "", name ?: "")
                            }
                            val actionToHomeFragment =
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                            findNavController().navigate(actionToHomeFragment)
                        }
                        Status.ERROR -> {
                            binding.layoutLoading.gone()
                            binding.btnSignIn.text =
                                requireContext().resources.getString(R.string.sign_in)
                            binding.btnSignIn.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.failed_login),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
        }
    }

    private fun isValidateString(str: String): Boolean =
        EMAIL_ADDRESS_PATTERN.matcher(str).matches()

    companion object {
        // email validate
        private val EMAIL_ADDRESS_PATTERN =
            Pattern.compile("[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }
}