package com.abdanzakialifian.storyapp.presentation.maps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Stories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val storyUseCase: StoryUseCase,
    private val storyDataStore: StoryDataStore
) : ViewModel() {
    private val _uiStateLocationUser = MutableStateFlow<List<Stories>>(listOf())
    val uiStateLocationUser: StateFlow<List<Stories>> = _uiStateLocationUser

    init {
        getLocation()
    }

    private fun getLocation() {
        viewModelScope.launch {
            storyDataStore.getToken()
                .collect { token ->
                    storyUseCase.getLocation("Bearer $token", NUMBER_OF_LOCATION)
                        .catch { throwable ->
                            Log.e(TAG, throwable.message.toString())
                        }
                        .collect { data ->
                            _uiStateLocationUser.value = data
                        }
                }
        }
    }

    companion object {
        // [1] get all stories with location
        private const val NUMBER_OF_LOCATION = 1
        private val TAG = MapsViewModel::class.java.simpleName
    }
}