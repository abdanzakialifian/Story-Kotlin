package com.abdanzakialifian.storyapp.data.source.remote.service

import com.abdanzakialifian.storyapp.data.source.remote.model.LoginResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.RegistrationResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.StoriesResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun registrationUser(@Body requestBody: RequestBody): RegistrationResponse

    @POST("login")
    suspend fun loginUser(@Body requestBody: RequestBody): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoriesResponse
}