package com.abdanzakialifian.storyapp.presentation.login

import android.text.Editable
import android.text.TextWatcher
import com.abdanzakialifian.storyapp.databinding.FragmentLoginBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment
import java.util.regex.Pattern

class LoginFragment : BaseVBFragment<FragmentLoginBinding>() {
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun initView() {
        validateEmail()
    }

    private fun validateEmail() {
        binding.apply {
            // get edittext from text input layout
            val email = edtEmail.editText
            email?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // remove error information if text empty
                    if (email.text?.isEmpty() == true) edtEmail.isErrorEnabled = false
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnSignIn.setOnClickListener {
                edtEmail.isErrorEnabled = !isValidateString(email?.text.toString())
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