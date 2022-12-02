package com.abdanzakialifian.storyapp.data.source.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdanzakialifian.storyapp.data.source.local.room.dao.RemoteKeysDao
import com.abdanzakialifian.storyapp.data.source.local.room.dao.StoriesDao
import com.abdanzakialifian.storyapp.data.source.local.room.entity.RemoteKeys
import com.abdanzakialifian.storyapp.data.source.local.room.entity.StoriesEntities
import javax.inject.Singleton

@Singleton
@Database(entities = [StoriesEntities::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun storyDao(): StoriesDao
    abstract fun remoteKeys(): RemoteKeysDao
}