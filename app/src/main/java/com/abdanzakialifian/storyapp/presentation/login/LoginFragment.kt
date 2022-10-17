package com.abdanzakialifian.storyapp.presentation.login

import com.abdanzakialifian.storyapp.databinding.FragmentLoginBinding
import com.abdanzakialifian.storyapp.presentation.base.BaseVBFragment

class LoginFragment : BaseVBFragment<FragmentLoginBinding>() {
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun initView() {}
}