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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryResponse> {
        return try {
            val position = params.key ?: INITIAL_PAGE
            val response = apiService.getAllStories(token ?: "", position, params.loadSize)
            val listStoryResponse = response.listStory as List<ListStoryResponse>

            LoadResult.Page(
                data = listStoryResponse,
                prevKey = if (position == INITIAL_PAGE) null else position - 1,
                nextKey = if (listStoryResponse.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    fun getToken(token: String) {
        this.token = token
    }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}