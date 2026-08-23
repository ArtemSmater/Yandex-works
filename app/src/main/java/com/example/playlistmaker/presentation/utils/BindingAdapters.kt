package com.example.playlistmaker.presentation.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track

@BindingAdapter("trackPoster")
fun bindTrackPoster(imageView: ImageView, track: Track) {
    val multiTransformation = MultiTransformation(
        CenterCrop(),
        RoundedCorners(Transform.dpToPx(8f, imageView.context))
    )

    Glide.with(imageView)
        .load(Transform.getHighQualityLink(track.artworkUrl100))
        .apply(RequestOptions.bitmapTransform(multiTransformation))
        .placeholder(R.drawable.placeholder)
        .into(imageView)
}

@BindingAdapter("millsToSeconds")
fun bindSeconds(textView: TextView, track: Track) {
    textView.text = Transform.millsToMins(track.trackTimeMillis)
}

@BindingAdapter("collectionName")
fun bindCollectionName(textView: TextView, text: String?) {
    if (text == null) {
        textView.visibility = View.GONE
    } else {
        textView.text = text
    }
}

@BindingAdapter("textVisibility")
fun setVisibility(textView: TextView, text: String?) {
    textView.isVisible = text != null
}

@BindingAdapter("trackYear")
fun bindTrackYear(textView: TextView, date: String?) {
    if (date == null) {
        textView.visibility = View.GONE
    } else {
        textView.text = Transform.getYear(date)
    }
}