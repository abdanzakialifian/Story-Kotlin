package com.abdanzakialifian.storyapp.presentation.registration.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.utils.Result
import com.abdanzakialifian.storyapp.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val storyUseCase: StoryUseCase) :
    ViewModel() {
    private val _uiStateRegistrationUser = MutableStateFlow<Result<Registration>>(Result.loading())
    val uiStateRegistrationUser: StateFlow<Result<Registration>> = _uiStateRegistrationUser

    fun registrationUser(requestBody: RequestBody) {
        viewModelScope.launch {
            storyUseCase.registrationUser(requestBody)
                .collect {
                    when (it.status) {
                        Status.LOADING -> _uiStateRegistrationUser.value = Result.loading()

                        Status.SUCCESS -> _uiStateRegistrationUser.value = Result.success(it.data)
                        Status.ERROR -> _uiStateRegistrationUser.value =
                            Result.error(it.message.toString())
                    }
                }
        }
    }
}