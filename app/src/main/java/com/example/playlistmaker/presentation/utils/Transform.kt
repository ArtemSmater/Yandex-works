package com.example.playlistmaker.presentation.utils

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale
fun Long.millsToMins(): String {
    return SimpleDateFormat("m:ss", Locale.getDefault()).format(this)
}

fun Float.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    ).toInt()
}

