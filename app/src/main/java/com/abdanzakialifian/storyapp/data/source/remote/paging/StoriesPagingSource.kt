package com.abdanzakialifian.storyapp.data.source.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abdanzakialifian.storyapp.data.source.remote.model.ListStoryResponse
import com.abdanzakialifian.storyapp.data.source.remote.service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoriesPagingSource @Inject constructor(private val apiService: ApiService) :
    PagingSource<Int, ListStoryResponse>() {

    private var token: String? = null

    override fun getRefreshKey(state: PagingState<Int, ListStoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryResponse> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getAllStories(token ?: "", position, params.loadSize)
            val listStoryResponse = response.listStory

            val listStory = ArrayList<ListStoryResponse>()
            if (listStoryResponse != null) {
                listStory.addAll(listStoryResponse)
            }

            LoadResult.Page(
                data = listStory,
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    fun getToken(token: String) {
        this.token = token
    }
}