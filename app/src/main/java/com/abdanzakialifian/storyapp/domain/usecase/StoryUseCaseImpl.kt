package com.abdanzakialifian.storyapp.domain.usecase

import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.domain.interfaces.StoryRepository
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.utils.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryUseCaseImpl @Inject constructor(private val storyRepository: StoryRepository) :
    StoryUseCase {
    override fun registrationUser(requestBody: RequestBody): Flow<Result<Registration>> =
        storyRepository.registrationUser(requestBody)

    override fun loginUser(requestBody: RequestBody): Flow<Result<Login>> =
        storyRepository.loginUser(requestBody)

    override fun getAllStories(token: String): Flow<Result<PagingData<Stories>>> =
        storyRepository.getAllStories(token)

    override fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): Flow<Result<NewStory>> =
        storyRepository.createNewStory(token, file, description, latitude, longitude)

    override fun getLocation(token: String, location: Int): Flow<List<Stories>> =
        storyRepository.getLocation(token, location)

    override suspend fun saveUserData(token: String, name: String) =
        storyRepository.saveUserData(token, name)

    override suspend fun saveLanguageCode(languageCode: String) =
        storyRepository.saveLanguageCode(languageCode)

    override suspend fun saveUserSession(isLogin: Boolean) =
        storyRepository.saveUserSession(isLogin)

    override suspend fun deleteDataStore() = storyRepository.deleteDataStore()

    override fun getUserSession(): Flow<Boolean> = storyRepository.getUserSession()

    override fun getLanguageCode(): Flow<String> = storyRepository.getLanguageCode()

    override fun getName(): Flow<String> = storyRepository.getName()

    override fun getToken(): Flow<String> = storyRepository.getToken()
}