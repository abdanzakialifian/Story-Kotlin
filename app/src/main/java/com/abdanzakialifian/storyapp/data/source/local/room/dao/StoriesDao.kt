package com.abdanzakialifian.storyapp.data.source.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abdanzakialifian.storyapp.data.source.local.room.entity.StoriesEntities

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoriesEntities>)

    @Query("SELECT * FROM stories_entities")
    fun getAllStories(): PagingSource<Int, StoriesEntities>

    @Query("DELETE FROM stories_entities")
    suspend fun deleteAll()
}