package com.abdanzakialifian.storyapp.data.di

import com.abdanzakialifian.storyapp.data.repository.StoryRepositoryImpl
import com.abdanzakialifian.storyapp.domain.interfaces.StoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StoryRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindStoryRepository(storyRepositoryImpl: StoryRepositoryImpl): StoryRepository
}