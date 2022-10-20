package com.abdanzakialifian.storyapp.presentation.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import com.abdanzakialifian.storyapp.data.source.local.datastore.StoryDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val storyDataStore: StoryDataStore) :
    ViewModel() {

    fun getUserSession(): Flow<Boolean> = storyDataStore.getUserSession()
}