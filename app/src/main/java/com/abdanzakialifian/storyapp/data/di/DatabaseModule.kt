package com.abdanzakialifian.storyapp.data.di

import android.content.Context
import androidx.room.Room
import com.abdanzakialifian.storyapp.data.source.local.room.dao.RemoteKeysDao
import com.abdanzakialifian.storyapp.data.source.local.room.dao.StoriesDao
import com.abdanzakialifian.storyapp.data.source.local.room.database.StoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): StoriesDatabase =
        Room.databaseBuilder(context, StoriesDatabase::class.java, "stories_database")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun provideStoriesDao(database: StoriesDatabase): StoriesDao = database.storyDao()

    @Provides
    fun provideRemoteKeysDao(database: StoriesDatabase): RemoteKeysDao = database.remoteKeys()
}