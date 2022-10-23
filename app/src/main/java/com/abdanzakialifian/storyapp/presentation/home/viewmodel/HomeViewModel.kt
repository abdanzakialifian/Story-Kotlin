package com.abdanzakialifian.storyapp.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyUseCase: StoryUseCase,
    private val storyDataStore: StoryDataStore
) : ViewModel() {

    private val _uiStateGetAllStories =
        MutableStateFlow<Result<PagingData<ListStory>>>(Result.loading())
    val uiStateGetAllStories get() = _uiStateGetAllStories

    init {
        getToken()
    }

    // get token from datastore
    private fun getToken() {
        viewModelScope.launch {
            storyDataStore.getToken().collect { token ->
                getAllStories(token)
            }
        }
    }

    private fun getAllStories(token: String) {
        _uiStateGetAllStories.value = Result.loading()
        viewModelScope.launch {
            storyUseCase.getAllStories("Bearer $token")
                .cachedIn(viewModelScope)
                .catch { throwable ->
                    _uiStateGetAllStories.value = Result.error(throwable.message.toString())
                }
                .collect { data ->
                    _uiStateGetAllStories.value = Result.success(data)
                }
        }
    }

    // save user session in datastore
    fun saveUserSession(isLogin: Boolean) {
        viewModelScope.launch {
            storyDataStore.saveUserSession(isLogin)
        }
    }
}