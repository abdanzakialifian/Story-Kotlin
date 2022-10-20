package com.abdanzakialifian.storyapp.utils

import com.abdanzakialifian.storyapp.data.source.remote.model.ListStoryResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.LoginResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.RegistrationResponse
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.LoginResult
import com.abdanzakialifian.storyapp.domain.model.Registration

object DataMapper {
    fun mapRegistrationResponseToRegistration(input: RegistrationResponse): Registration =
        Registration(
            error = input.error,
            message = input.message
        )

    fun mapLoginResponseToLogin(input: LoginResponse): Login {
        val loginResult = LoginResult(
            name = input.loginResult?.name,
            userId = input.loginResult?.userId,
            token = input.loginResult?.token
        )
        return Login(
            loginResult = loginResult,
            error = input.error,
            message = input.message
        )
    }

    fun mapListStoryResponseToListStory(input: ListStoryResponse): ListStory = ListStory(
        photoUrl = input.photoUrl,
        createdAt = input.createdAt,
        name = input.name,
        description = input.description,
        lon = input.lon,
        id = input.id,
        lat = input.lat
    )
}