package com.abdanzakialifian.storyapp.data.source.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abdanzakialifian.storyapp.data.source.local.room.dao.StoriesDao
import com.abdanzakialifian.storyapp.data.source.local.room.entity.StoriesEntities
import javax.inject.Singleton

@Singleton
@Database(entities = [StoriesEntities::class], version = 1)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun storyDao(): StoriesDao
}