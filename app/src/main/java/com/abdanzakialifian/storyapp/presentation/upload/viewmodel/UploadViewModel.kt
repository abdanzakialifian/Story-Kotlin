package com.abdanzakialifian.storyapp.presentation.upload.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val storyUseCase: StoryUseCase,
    private val storyDataStore: StoryDataStore
) : ViewModel() {
    private val _uiStateNewStory = MutableStateFlow<Result<NewStory>>(Result.loading())
    val uiStateNewStory: StateFlow<Result<NewStory>> = _uiStateNewStory

    fun newStory(file: MultipartBody.Part, description: RequestBody) {
        _uiStateNewStory.value = Result.loading()
        viewModelScope.launch {
            // get token from datastore
            storyDataStore.getToken().collect { token ->
                storyUseCase.createNewStory("Bearer $token", file, description)
                    .catch { throwable ->
                        _uiStateNewStory.value = Result.error(throwable.message.toString())
                    }
                    .collect { data ->
                        _uiStateNewStory.value = Result.success(data)
                    }
            }
        }
    }
}