package com.abdanzakialifian.storyapp.presentation.registration.view

import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentRegistrationBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import com.abdanzakialifian.storyapp.presentation.registration.viewmodel.RegistrationViewModel
import com.abdanzakialifian.storyapp.utils.Status
import com.abdanzakialifian.storyapp.utils.gone
import com.abdanzakialifian.storyapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.regex.Pattern

@AndroidEntryPoint
class RegistrationFragment : BaseVBFragment<FragmentRegistrationBinding>() {

    private val viewModel by viewModels<RegistrationViewModel>()

    override fun getViewBinding(): FragmentRegistrationBinding =
        FragmentRegistrationBinding.inflate(layoutInflater)

    override fun initView() {
        registration()
        binding.tvSignIn.setOnClickListener {
            val actionToLoginFragment =
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            findNavController().navigate(actionToLoginFragment)
        }
    }

    private fun registration() {
        binding.apply {
            // get edittext from text input layout
            val name = edtName.editText
            val email = edtEmail.editText
            val password = edtPassword.editText
            btnSignUp.setOnClickListener {
                if (name?.text?.isEmpty() == true && email?.text?.isEmpty() == true && password?.text?.isEmpty() == true) {
                    edtName.isErrorEnabled = true
                    edtName.error = resources.getString(R.string.name_cannot_be_empty)
                    edtEmail.isErrorEnabled = !isValidateString(email.text.toString())
                    edtPassword.isErrorEnabled = password.text?.isEmpty() == true
                } else {
                    binding.layoutLoading.visible()
                    binding.btnSignUp.text = ""
                    binding.btnSignUp.isEnabled = false
                    setRegistrationUser(name, email, password)
                }
            }
        }
    }

    private fun setRegistrationUser(name: EditText?, email: EditText?, password: EditText?) {
        val jsonObject = JSONObject().apply {
            put(NAME, name?.text?.toString())
            put(EMAIL, email?.text?.toString()?.trim())
            put(PASSWORD, password?.text?.toString()?.trim())
        }.toString()

        val requestBody = jsonObject.toRequestBody("application/json".toMediaTypeOrNull())

        lifecycleScope.launchWhenStarted {
            viewModel.registrationUser(requestBody)
            viewModel.uiStateRegistrationUser
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    when (it.status) {
                        Status.LOADING -> {
                            binding.layoutLoading.visible()
                            binding.btnSignUp.text = ""
                            binding.btnSignUp.isEnabled = false
                        }
                        Status.SUCCESS -> {
                            binding.layoutLoading.gone()
                            binding.btnSignUp.text =
                                requireContext().resources.getString(R.string.signup)
                            binding.btnSignUp.isEnabled = true
                            setAlertDialog()
                        }
                        Status.ERROR -> {
                            binding.layoutLoading.gone()
                            binding.btnSignUp.text =
                                requireContext().resources.getString(R.string.signup)
                            binding.btnSignUp.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.failed_registration),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }
    }

    private fun setAlertDialog() {
        val builder = AlertDialog.Builder(requireContext()).create()
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog_success_registration, null)
        val btnOk = view.findViewById<Button>(R.id.btn_ok)
        builder.setView(view)
        builder.setCanceledOnTouchOutside(false)
        btnOk.setOnClickListener {
            val actionToLoginFragment =
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            findNavController().navigate(actionToLoginFragment)
            builder.dismiss()
        }
        builder.show()
    }

    private fun isValidateString(str: String): Boolean =
        EMAIL_ADDRESS_PATTERN.matcher(str).matches()

    companion object {
        // email validate
        private val EMAIL_ADDRESS_PATTERN =
            Pattern.compile("[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }
}