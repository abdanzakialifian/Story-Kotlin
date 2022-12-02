package com.abdanzakialifian.storyapp.utils

import com.abdanzakialifian.storyapp.domain.model.*
import com.abdanzakialifian.storyapp.presentation.login.view.LoginFragment
import com.abdanzakialifian.storyapp.presentation.registration.view.RegistrationFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

object DataDummy {
    fun dummyLoginResponseSuccess(): Login {
        val loginResult = LoginResult(
            name = "Abdan Zaki Alifian",
            userId = "290699",
            token = "1q2w3e4r5t6y7u8i9o0p"
        )

        return Login(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    fun dummyLoginResponseFailed(): Login = Login(
        loginResult = null,
        error = true,
        message = "must be a valid email"
    )

    fun dummyLoginRequest(): RequestBody {
        val jsonObject = JSONObject().apply {
            put(LoginFragment.EMAIL, "unittest@gmail.com")
            put(LoginFragment.PASSWORD, "unittest123")
        }.toString()
        return jsonObject.toRequestBody("application/json".toMediaTypeOrNull())
    }

    fun dummyRegistrationResponseSuccess(): Registration = Registration(
        error = false,
        message = "User created"
    )

    fun dummyRegistrationResponseFailed(): Registration = Registration(
        error = true,
        message = "login failed"
    )

    fun dummyRegistrationRequest(): RequestBody {
        val jsonObject = JSONObject().apply {
            put(RegistrationFragment.NAME, "Abdan Zaki Alifian")
            put(RegistrationFragment.EMAIL, "unittest@gmail.com")
            put(RegistrationFragment.PASSWORD, "unittest123")
        }.toString()

        return jsonObject.toRequestBody("application/json".toMediaTypeOrNull())
    }

    fun dummyCreateNewStoryResponseSuccess(): NewStory = NewStory(
        error = false,
        message = "Story created successfully"
    )

    fun dummyCreateNewStoryResponseFailed(): NewStory = NewStory(
        error = true,
        message = "Story created failed"
    )

    fun dummyImageFile(): MultipartBody.Part {
        val reduceFile =
            File("/storage/emulated/0/Android/data/com.abdanzakialifian.storyapp/files/Pictures/29-Nov-20222307952967611922747.jpg")
        val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "photo",
            reduceFile.name,
            requestImageFile
        )
    }

    fun dummyDescription(): RequestBody {
        val description = "New story in story app."
        return description.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun dummyLatitude(): RequestBody? {
        val latitude = -6.174786544973977
        return latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun dummyLongitude(): RequestBody? {
        val longitude = 106.82720257559791
        return longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun dummyStoriesResponse(): List<Stories> {
        val stories = ArrayList<Stories>()

        for (i in 0..50) {
            val model = Stories(
                photoUrl = "https://images8.alphacoders.com/112/1128982.jpg",
                createdAt = "2022-12-02T02:20:44.537Z",
                name = "Abdan Zaki Alifian",
                description = "Unit testing story app",
                lon = 106.82720257559791,
                id = "1",
                lat = -6.174786544973977
            )
            stories.add(model)
        }

        return stories
    }
}