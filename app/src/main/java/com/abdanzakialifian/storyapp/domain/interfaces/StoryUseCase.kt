package com.abdanzakialifian.storyapp.domain.interfaces

import androidx.paging.PagingData
import com.abdanzakialifian.storyapp.domain.model.ListStory
import com.abdanzakialifian.storyapp.domain.model.Login
import com.abdanzakialifian.storyapp.domain.model.NewStory
import com.abdanzakialifian.storyapp.domain.model.Registration
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryUseCase {
    fun registrationUser(requestBody: RequestBody): Flow<Registration>
    fun loginUser(requestBody: RequestBody): Flow<Login>
    fun getAllStories(token: String): Flow<PagingData<ListStory>>
    fun createNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<NewStory>
}