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

    init {
        getDataStoreName()
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
}