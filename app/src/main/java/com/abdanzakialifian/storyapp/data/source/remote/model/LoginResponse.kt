package com.abdanzakialifian.storyapp.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@SerializedName("loginResult")
	val loginResult: LoginResultResponse? = null,

	@SerializedName("error")
	val error: Boolean? = null,

	@SerializedName("message")
	val message: String? = null
)

data class LoginResultResponse(
	@SerializedName("name")
	val name: String? = null,

	@SerializedName("userId")
	val userId: String? = null,

	@SerializedName("token")
	val token: String? = null
)

