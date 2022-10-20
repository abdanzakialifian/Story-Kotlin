package com.abdanzakialifian.storyapp.presentation.login.view

import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    override fun initView() {
        login()
        // navigate to registration page
        binding.tvRegister.setOnClickListener {
            val actionToRegistrationFragment =
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(actionToRegistrationFragment)
        }
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
                    setLogin(email, password)
                }
            }
        }
    }

    private fun setLogin(email: EditText?, password: EditText?) {
        val jsonObject = JSONObject()
        jsonObject.put(EMAIL, email?.text?.toString()?.trim())
        jsonObject.put(PASSWORD, password?.text?.toString()?.trim())

        val jsonObjectToString = jsonObject.toString()
        val requestBody = jsonObjectToString.toRequestBody("application/json".toMediaTypeOrNull())

        lifecycleScope.launchWhenStarted {
            viewModel.loginUser(requestBody)
            viewModel.uiStateLoginUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.progressBar.visible()
                        }
                        Status.SUCCESS -> {
                            binding.progressBar.gone()
                            Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
                                .show()
                            val token = it.data?.loginResult?.token
                            lifecycleScope.launchWhenStarted {
                                viewModel.saveToken(token ?: "")
                            }
                            val actionToHomeFragment =
                                LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                            findNavController().navigate(actionToHomeFragment)
                        }
                        Status.ERROR -> {
                            binding.progressBar.gone()
                            Toast.makeText(requireContext(), it.data?.message, Toast.LENGTH_SHORT)
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
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}