package com.example.playlistmaker.presentation.utils

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

class Transform {
    companion object {
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

        fun millsToMins(mills: Long): String {
            return SimpleDateFormat("m:ss", Locale.getDefault()).format(mills)
        }

        fun getHighQualityLink(baseLink: String): String {
            return if (baseLink.contains(LOW_QUALITY_ENDPOINT)) {
                baseLink.replace(LOW_QUALITY_ENDPOINT, HIGH_QUALITY_ENDPOINT)
            } else {
                baseLink
            }
        }

        fun getYear(date: String) : String {
            return date.substringBefore("-")
        }

        private const val HIGH_QUALITY_ENDPOINT = "512x512bb.jpg"
        private const val LOW_QUALITY_ENDPOINT = "100x100bb.jpg"
    }
}