package com.abdanzakialifian.storyapp.presentation.registration

import com.abdanzakialifian.storyapp.R
import com.abdanzakialifian.storyapp.databinding.FragmentRegistrationBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import java.util.regex.Pattern

class RegistrationFragment : BaseVBFragment<FragmentRegistrationBinding>() {
    override fun getViewBinding(): FragmentRegistrationBinding =
        FragmentRegistrationBinding.inflate(layoutInflater)

    override fun initView() {
        binding.apply {
            // get edittext from text input layout
            val name = edtName.editText
            val email = edtEmail.editText
            val password = edtPassword.editText
            btnSignUp.setOnClickListener {
                if (name?.text?.isEmpty() == true) {
                    edtName.isErrorEnabled = true
                    edtName.error = resources.getString(R.string.name_cannot_be_empty)
                }

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