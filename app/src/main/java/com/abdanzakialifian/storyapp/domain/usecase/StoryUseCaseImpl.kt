package com.abdanzakialifian.storyapp.domain.usecase

import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.data.repository.StoryRepositoryImpl
import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.domain.model.Registration
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryUseCaseImpl @Inject constructor(private val storyRepositoryImpl: StoryRepositoryImpl) :
    StoryUseCase {
    override fun registrationUser(requestBody: RequestBody): Flow<Registration> =
        storyRepositoryImpl.registrationUser(requestBody)

    override fun loginUser(requestBody: RequestBody): Flow<Login> =
        storyRepositoryImpl.loginUser(requestBody)

    override fun getAllStories(token: String): Flow<PagingData<ListStory>> =
        storyRepositoryImpl.getAllStories(token)

    override fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<NewStory> = storyRepositoryImpl.createNewStory(token, file, description)
}