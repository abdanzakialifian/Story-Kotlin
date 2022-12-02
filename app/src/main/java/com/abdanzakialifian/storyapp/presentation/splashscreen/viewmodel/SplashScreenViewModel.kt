package com.abdanzakialifian.storyapp.presentation.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val storyUseCase: StoryUseCase) :
    ViewModel() {

    private val _getUserSession = MutableStateFlow(false)
    val getUserSession: StateFlow<Boolean> = _getUserSession

    private val _getLanguageCode = MutableStateFlow("")
    val getLanguageCode: StateFlow<String> = _getLanguageCode

    fun getUserSession() {
        viewModelScope.launch {
            storyUseCase.getUserSession()
                .collect { isLogin ->
                    _getUserSession.value = isLogin
                }
        }
    }

    fun getLanguageCode() {
        viewModelScope.launch {
            storyUseCase.getLanguageCode()
                .collect { languageCode ->
                    _getLanguageCode.value = languageCode
                }
        }
    }
}