package com.abdanzakialifian.storyapp.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class NewStoryResponse(
    @SerializedName("error")
    val error: Boolean? = null,

    @SerializedName("message")
    val message: String? = null
)