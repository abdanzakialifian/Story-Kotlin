package com.abdanzakialifian.storyapp.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val storyDataStore: StoryDataStore) :
    ViewModel() {

    private val _userName = MutableStateFlow(toString())
    val userName: StateFlow<String> = _userName

    private val _languageCode = MutableStateFlow(toString())
    val languageCode: StateFlow<String> = _languageCode

    init {
        getDataStoreName()
        getLanguageCode()
    }

    // delete datastore
    fun deleteDataStore() {
        viewModelScope.launch {
            storyDataStore.deleteDataStore()
        }
    }

    private fun getDataStoreName() {
        viewModelScope.launch {
            storyDataStore.getName()
                .collect { data ->
                    _userName.value = data
                }
        }
    }

    fun saveLanguageCode(languageCode: String) {
        viewModelScope.launch {
            storyDataStore.saveLanguageCode(languageCode)
        }
    }

    private fun getLanguageCode() {
        viewModelScope.launch {
            storyDataStore.getLanguageCode()
                .collect { data ->
                    _languageCode.value = data
                }
        }
    }
}