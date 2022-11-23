package com.abdanzakialifian.storyapp.domain.usecase

import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.domain.interfaces.StoryRepository
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.domain.model.Stories
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryUseCaseImpl @Inject constructor(private val storyRepository: StoryRepository) :
    StoryUseCase {
    override fun registrationUser(requestBody: RequestBody): Flow<Registration> =
        storyRepository.registrationUser(requestBody)

    override fun loginUser(requestBody: RequestBody): Flow<Login> =
        storyRepository.loginUser(requestBody)

    override fun getAllStories(token: String): Flow<PagingData<Stories>> =
        storyRepository.getAllStories(token)

    override fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<NewStory> = storyRepository.createNewStory(token, file, description)

    override fun getLocation(token: String, location: Int): Flow<List<Stories>> =
        storyRepository.getLocation(token, location)
}