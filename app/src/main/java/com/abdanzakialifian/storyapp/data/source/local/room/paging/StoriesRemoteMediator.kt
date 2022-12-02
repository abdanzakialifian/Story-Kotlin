package com.abdanzakialifian.storyapp.data.source.local.room.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.abdanzakialifian.storyapp.data.source.local.room.database.StoriesDatabase
import com.abdanzakialifian.storyapp.data.source.local.room.entity.RemoteKeys
import com.abdanzakialifian.storyapp.data.source.local.room.entity.StoriesEntities
import com.abdanzakialifian.storyapp.data.source.remote.service.ApiService
import com.abdanzakialifian.storyapp.utils.DataMapper

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator(
    private val database: StoriesDatabase,
    private val apiService: ApiService,
    private val token: String
) : RemoteMediator<Int, StoriesEntities>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoriesEntities>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val response = apiService.getAllStories(token, page, state.config.pageSize)
            val endPaginationReached = response.listStory.isNullOrEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeys().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endPaginationReached) null else page + 1
                val remoteKeys = response.listStory?.map {
                    RemoteKeys(id = it.id ?: "", prevKey = prevKey, nextKey = nextKey)
                }

                database.remoteKeys().insertAll(remoteKeys)

                val mapping = DataMapper.mapListStoryResponseToStoriesEntities(response.listStory)
                database.storyDao().insertStories(mapping)
            }
            return MediatorResult.Success(endOfPaginationReached = endPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyFirstItem(state: PagingState<Int, StoriesEntities>): RemoteKeys? {
        val loadResult = state.pages.firstOrNull {
            it.data.isNotEmpty()
        }
        return loadResult?.data?.firstOrNull()?.let { data ->
            database.remoteKeys().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyLastItem(state: PagingState<Int, StoriesEntities>): RemoteKeys? {
        val loadResult = state.pages.lastOrNull {
            it.data.isNotEmpty()
        }
        return loadResult?.data?.lastOrNull()?.let { data ->
            database.remoteKeys().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoriesEntities>): RemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeys().getRemoteKeysId(id)
            }
        }

    companion object {
        private const val INITIAL_PAGE = 1
    }
}