package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import com.example.playlistmaker.pojo.Track
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
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mills)
        }

        fun editList(item: Track, list: MutableList<Track>): List<Track> {
            with(list) {
                if (contains(item)) remove(item)
                add(0, item)
                if (size > 10) removeAt(lastIndex)
            }
            return list
        }
    }
}