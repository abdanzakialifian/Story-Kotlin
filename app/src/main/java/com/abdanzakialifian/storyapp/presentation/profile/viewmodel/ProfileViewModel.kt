package com.abdanzakialifian.storyapp.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val storyUseCase: StoryUseCase) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _languageCode = MutableStateFlow("")
    val languageCode: StateFlow<String> = _languageCode

    // delete datastore
    fun deleteDataStore() {
        viewModelScope.launch {
            storyUseCase.deleteDataStore()
        }
    }

    fun getDataStoreName() {
        viewModelScope.launch {
            storyUseCase.getName()
                .collect { data ->
                    _userName.value = data
                }
        }
    }

    fun saveLanguageCode(languageCode: String) {
        viewModelScope.launch {
            storyUseCase.saveLanguageCode(languageCode)
        }
    }

    fun getLanguageCode() {
        viewModelScope.launch {
            storyUseCase.getLanguageCode()
                .collect { data ->
                    _languageCode.value = data
                }
        }
    }
}