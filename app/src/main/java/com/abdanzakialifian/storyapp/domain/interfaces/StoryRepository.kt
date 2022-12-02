package com.abdanzakialifian.storyapp.domain.interfaces

import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.utils.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepository {
    fun registrationUser(requestBody: RequestBody): Flow<Result<Registration>>
    fun loginUser(requestBody: RequestBody): Flow<Result<Login>>
    fun getAllStories(token: String): Flow<Result<PagingData<Stories>>>
    fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): Flow<Result<NewStory>>

    fun getLocation(token: String, location: Int): Flow<List<Stories>>
    fun getUserSession(): Flow<Boolean>
    fun getLanguageCode(): Flow<String>
    fun getName(): Flow<String>
    fun getToken(): Flow<String>
    suspend fun saveUserData(token: String, name: String)
    suspend fun saveLanguageCode(languageCode: String)
    suspend fun saveUserSession(isLogin: Boolean)
    suspend fun deleteDataStore()
}