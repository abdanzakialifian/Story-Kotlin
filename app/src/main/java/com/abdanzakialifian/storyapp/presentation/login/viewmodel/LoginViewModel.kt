package com.abdanzakialifian.storyapp.presentation.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val storyUseCase: StoryUseCase,
    private val storyDataStore: StoryDataStore
) : ViewModel() {
    private val _uiStateLoginUser = MutableStateFlow<Result<Login>>(Result.loading())
    val uiStateLoginUser: StateFlow<Result<Login>> = _uiStateLoginUser

    fun loginUser(requestBody: RequestBody) {
        _uiStateLoginUser.value = Result.loading()
        viewModelScope.launch {
            storyUseCase.loginUser(requestBody)
                .catch { throwable ->
                    _uiStateLoginUser.value = Result.error(throwable.message.toString())
                }
                .collect { data ->
                    _uiStateLoginUser.value = Result.success(data)
                }
        }
    }

    // save token in datastore
    fun saveUserData(token: String, name: String) {
        viewModelScope.launch {
            storyDataStore.saveUserData(token, name)
        }
    }
}