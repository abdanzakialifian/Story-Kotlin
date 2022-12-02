package com.abdanzakialifian.storyapp.presentation.upload.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val storyUseCase: StoryUseCase) : ViewModel() {
    private val _getToken = MutableStateFlow("")
    val getToken: StateFlow<String> = _getToken

    private val _uiStateNewStory = MutableStateFlow<Result<NewStory>>(Result.loading())
    val uiStateNewStory: StateFlow<Result<NewStory>> = _uiStateNewStory

    fun getToken() {
        viewModelScope.launch {
            // get token from datastore
            storyUseCase.getToken().collect { token ->
                _getToken.value = token
            }
        }
    }

    fun newStory(
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?,
        token: String
    ) {
        viewModelScope.launch {
            storyUseCase.createNewStory("Bearer $token", file, description, latitude, longitude)
                .collect {
                    when (it.status) {
                        Status.LOADING -> _uiStateNewStory.value = Result.loading()
                        Status.SUCCESS -> _uiStateNewStory.value = Result.success(it.data)
                        Status.ERROR -> _uiStateNewStory.value = Result.error(it.message.toString())
                    }
                }
        }
    }
}