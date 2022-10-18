package com.abdanzakialifian.storyapp.presentation.login

import com.abdanzakialifian.storyapp.databinding.FragmentLoginBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import java.util.regex.Pattern

class LoginFragment : BaseVBFragment<FragmentLoginBinding>() {
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun initView() {
        binding.apply {
            // get edittext from text input layout
            val email = edtEmail.editText
            val password = edtPassword.editText
            btnSignIn.setOnClickListener {
                edtEmail.isErrorEnabled = !isValidateString(email?.text.toString())
                edtPassword.isErrorEnabled = password?.text?.isEmpty() == true
            }
        }
    }

    private fun isValidateString(str: String): Boolean =
        EMAIL_ADDRESS_PATTERN.matcher(str).matches()

    companion object {
        // email validate
        private val EMAIL_ADDRESS_PATTERN =
            Pattern.compile("[a-zA-Z0-9+._%\\-]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+")
    }
}