package com.abdanzakialifian.storyapp.data.source.remote.source

import com.abdanzakialifian.storyapp.data.source.remote.model.LoginResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.NewStoryResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.RegistrationResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.StoriesResponse
import com.abdanzakialifian.storyapp.data.source.remote.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
) {
    fun registrationUser(requestBody: RequestBody): Flow<RegistrationResponse> = flow {
        val response = apiService.registrationUser(requestBody)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun loginUser(requestBody: RequestBody): Flow<LoginResponse> = flow {
        val response = apiService.loginUser(requestBody)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): Flow<NewStoryResponse> =
        flow {
            val response = apiService.createNewStory(token, file, description, latitude, longitude)
            emit(response)
        }.flowOn(Dispatchers.IO)

    fun getLocation(token: String, location: Int): Flow<StoriesResponse> = flow {
        val response = apiService.getLocation(token, location)
        emit(response)
    }.flowOn(Dispatchers.IO)
}