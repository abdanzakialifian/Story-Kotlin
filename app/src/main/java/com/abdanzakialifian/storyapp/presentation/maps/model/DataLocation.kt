package com.abdanzakialifian.storyapp.presentation.maps.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataLocation(
    val latitude: Double,
    val longitude: Double,
    val thoroughfare: String
) : Parcelable