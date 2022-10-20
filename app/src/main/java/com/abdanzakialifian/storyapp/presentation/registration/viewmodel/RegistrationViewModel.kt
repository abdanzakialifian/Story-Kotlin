package com.abdanzakialifian.storyapp.presentation.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val storyUseCase: StoryUseCase) :
    ViewModel() {
    private val _uiStateRegistrationUser = MutableStateFlow<Result<Registration>>(Result.loading())
    val uiStateRegistrationUser: StateFlow<Result<Registration>> = _uiStateRegistrationUser

    fun registrationUser(requestBody: RequestBody) {
        _uiStateRegistrationUser.value = Result.loading()
        viewModelScope.launch {
            storyUseCase.registrationUser(requestBody)
                .catch { throwable ->
                    _uiStateRegistrationUser.value = Result.error(throwable.message.toString())
                }
                .collect { data ->
                    _uiStateRegistrationUser.value = Result.success(data)
                }
        }
    }
}