package com.abdanzakialifian.storyapp.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadImageUrl(url: String) {
    Glide.with(context)
        .load(url)
        .into(this)
}