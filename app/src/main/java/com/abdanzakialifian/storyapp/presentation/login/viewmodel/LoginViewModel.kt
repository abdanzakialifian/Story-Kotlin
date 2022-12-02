package com.abdanzakialifian.storyapp.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storyUseCase: StoryUseCase
) : ViewModel() {
    private val _uiStateLoginUser = MutableStateFlow<Result<Login>>(Result.loading())
    val uiStateLoginUser: StateFlow<Result<Login>> = _uiStateLoginUser

    fun loginUser(requestBody: RequestBody) {
        viewModelScope.launch {
            storyUseCase.loginUser(requestBody)
                .collect {
                    when (it.status) {
                        Status.LOADING -> _uiStateLoginUser.value = Result.loading()
                        Status.SUCCESS -> _uiStateLoginUser.value = Result.success(it.data)
                        Status.ERROR -> _uiStateLoginUser.value =
                            Result.error(it.message.toString())
                    }
                }
        }
    }

    // save token in datastore
    fun saveUserData(token: String, name: String) {
        viewModelScope.launch {
            storyUseCase.saveUserData(token, name)
        }
    }
}