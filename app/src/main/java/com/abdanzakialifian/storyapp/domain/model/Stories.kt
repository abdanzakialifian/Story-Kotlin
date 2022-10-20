package com.abdanzakialifian.storyapp.domain.model

data class Stories(
    val listStory: List<ListStory>? = null,
    val error: Boolean? = null,
    val message: String? = null
)

data class ListStory(
    val photoUrl: String? = null,
    val createdAt: String? = null,
    val name: String? = null,
    val description: String? = null,
    val lon: Double? = null,
    val id: String? = null,
    val lat: Double? = null
)