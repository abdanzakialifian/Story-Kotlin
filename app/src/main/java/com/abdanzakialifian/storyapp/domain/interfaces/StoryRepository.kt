package com.abdanzakialifian.storyapp.domain.interfaces

import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.Registration
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface StoryRepository {
    fun registrationUser(requestBody: RequestBody): Flow<Registration>
    fun loginUser(requestBody: RequestBody): Flow<Login>
    fun getAllStories(token: String): Flow<PagingData<ListStory>>
}