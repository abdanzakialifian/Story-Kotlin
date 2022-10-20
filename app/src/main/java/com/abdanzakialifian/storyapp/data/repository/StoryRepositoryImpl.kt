package com.abdanzakialifian.storyapp.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.abdanzakialifian.storyapp.data.source.remote.source.RemoteDataSource
import com.abdanzakialifian.storyapp.domain.interfaces.StoryRepository
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    StoryRepository {
    override fun registrationUser(requestBody: RequestBody): Flow<Registration> =
        remoteDataSource.registrationUser(requestBody)
            .map { data ->
                DataMapper.mapRegistrationResponseToRegistration(data)
            }

    override fun loginUser(requestBody: RequestBody): Flow<Login> =
        remoteDataSource.loginUser(requestBody)
            .map { data ->
                DataMapper.mapLoginResponseToLogin(data)
            }

    override fun getAllStories(token: String): Flow<PagingData<ListStory>> =
        remoteDataSource.getAllStories(token).map {
            it.map { data ->
                DataMapper.mapListStoryResponseToListStory(data)
            }
        }
}