package com.abdanzakialifian.storyapp.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _getToken = MutableStateFlow("")
    val getToken: StateFlow<String> = _getToken

    private val _uiStateGetAllStories =
        MutableStateFlow<Result<PagingData<Stories>>>(Result.loading())
    val uiStateGetAllStories: StateFlow<Result<PagingData<Stories>>> = _uiStateGetAllStories

    // get token from datastore
    fun getToken() {
        viewModelScope.launch {
            // get token from datastore
            storyUseCase.getToken().collect { token ->
                _getToken.value = token
            }
        }
    }

    fun getAllStories(token: String) {
        _uiStateGetAllStories.value = Result.loading()
        viewModelScope.launch {
            storyUseCase.getAllStories("Bearer $token")
                .collect {
                    when (it.status) {
                        Status.LOADING -> _uiStateGetAllStories.value = Result.loading()
                        Status.SUCCESS -> _uiStateGetAllStories.value = Result.success(it.data)
                        Status.ERROR -> _uiStateGetAllStories.value =
                            Result.error(it.message.toString())
                    }
                }
        }
    }

    // save user session in datastore
    fun saveUserSession(isLogin: Boolean) {
        viewModelScope.launch {
            storyUseCase.saveUserSession(isLogin)
        }
    }
}