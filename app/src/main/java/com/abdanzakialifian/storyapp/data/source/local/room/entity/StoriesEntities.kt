package com.abdanzakialifian.storyapp.data.source.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories_entities")
data class StoriesEntities(
    @PrimaryKey
    val id: String? = null,
    val photoUrl: String? = null,
    val createdAt: String? = null,
    val name: String? = null,
    val description: String? = null,
    val lon: Double? = null,
    val lat: Double? = null
)