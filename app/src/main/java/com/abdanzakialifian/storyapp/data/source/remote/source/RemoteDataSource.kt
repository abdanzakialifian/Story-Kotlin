package com.abdanzakialifian.storyapp.data.source.remote.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.data.source.remote.model.ListStoryResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.LoginResponse
import com.abdanzakialifian.storyapp.data.source.remote.model.RegistrationResponse
import com.abdanzakialifian.storyapp.data.source.remote.paging.StoriesPagingSource
import com.abdanzakialifian.storyapp.data.source.remote.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val storiesPagingSource: StoriesPagingSource
) {
    fun registrationUser(requestBody: RequestBody): Flow<RegistrationResponse> = flow {
        val response = apiService.registrationUser(requestBody)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun loginUser(requestBody: RequestBody): Flow<LoginResponse> = flow {
        val response = apiService.loginUser(requestBody)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getAllStories(token: String): Flow<PagingData<ListStoryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = true,
                initialLoadSize = 15,
            ),
            pagingSourceFactory = {
                storiesPagingSource.getToken(token)
                storiesPagingSource
            }
        ).flow.flowOn(Dispatchers.IO)
    }
}