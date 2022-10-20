package com.abdanzakialifian.storyapp.domain.di

import com.abdanzakialifian.storyapp.domain.interfaces.StoryUseCase
import com.abdanzakialifian.storyapp.domain.usecase.StoryUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class StoryUseCaseModule {
    @Binds
    @ViewModelScoped
    abstract fun bindStoryUseCase(storyUseCaseImpl: StoryUseCaseImpl): StoryUseCase
}