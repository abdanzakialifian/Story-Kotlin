package com.abdanzakialifian.storyapp.data.repository

import androidx.paging.*
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import com.abdanzakialifian.storyapp.data.source.local.room.database.StoriesDatabase
import com.abdanzakialifian.storyapp.data.source.local.room.paging.StoriesRemoteMediator
import com.abdanzakialifian.storyapp.data.source.remote.service.ApiService
import com.abdanzakialifian.storyapp.data.source.remote.source.RemoteDataSource
import com.abdanzakialifian.storyapp.domain.interfaces.StoryRepository
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.domain.model.Registration
import com.abdanzakialifian.storyapp.domain.model.Stories
import com.abdanzakialifian.storyapp.utils.DataMapper
import com.abdanzakialifian.storyapp.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val apiService: ApiService,
    private val database: StoriesDatabase,
    private val storyDataStore: StoryDataStore
) :
    StoryRepository {
    override fun registrationUser(requestBody: RequestBody): Flow<Result<Registration>> = flow {
        emit(Result.loading())
        remoteDataSource.registrationUser(requestBody)
            .catch { throwable ->
                emit(Result.error(throwable.message.toString()))
            }
            .map { data ->
                DataMapper.mapRegistrationResponseToRegistration(data)
            }
            .collect { data ->
                emit(Result.success(data))
            }
    }

    override fun loginUser(requestBody: RequestBody): Flow<Result<Login>> = flow {
        emit(Result.loading())
        remoteDataSource.loginUser(requestBody)
            .catch { throwable ->
                emit(Result.error(throwable.message.toString()))
            }
            .map { data ->
                DataMapper.mapLoginResponseToLogin(data)
            }
            .collect { data ->
                emit(Result.success(data))
            }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getAllStories(token: String): Flow<Result<PagingData<Stories>>> = flow {
        emit(Result.loading())
        val pager = Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10
            ),
            remoteMediator = StoriesRemoteMediator(database, apiService, token),
            pagingSourceFactory = {
                database.storyDao().getAllStories()
            }
        ).flow.flowOn(Dispatchers.IO)

        pager
            .catch { throwable ->
                emit(Result.error(throwable.message.toString()))
            }
            .map { data ->
                data.map {
                    DataMapper.mapStoriesEntitiesToStories(it)
                }
            }
            .collect { data ->
                emit(Result.success(data))
            }
    }

    override fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): Flow<Result<NewStory>> = flow {
        emit(Result.loading())
        remoteDataSource.createNewStory(token, file, description, latitude, longitude)
            .catch { throwable ->
                emit(Result.error(throwable.message.toString()))
            }
            .map { data ->
                DataMapper.mapNewStoryResponseToNewStory(data)
            }
            .collect { data ->
                emit(Result.success(data))
            }
    }

    override fun getLocation(token: String, location: Int): Flow<List<Stories>> =
        remoteDataSource.getLocation(token, location).map { data ->
            DataMapper.mapStoryResponseToListStory(data)
        }

    override suspend fun saveUserData(token: String, name: String) =
        storyDataStore.saveUserData(token, name)

    override suspend fun saveLanguageCode(languageCode: String) =
        storyDataStore.saveLanguageCode(languageCode)

    override suspend fun saveUserSession(isLogin: Boolean) = storyDataStore.saveUserSession(isLogin)

    override suspend fun deleteDataStore() = storyDataStore.deleteDataStore()

    override fun getUserSession(): Flow<Boolean> = storyDataStore.getUserSession()

    override fun getLanguageCode(): Flow<String> = storyDataStore.getLanguageCode()

    override fun getName(): Flow<String> = storyDataStore.getName()

    override fun getToken(): Flow<String> = storyDataStore.getToken()
}