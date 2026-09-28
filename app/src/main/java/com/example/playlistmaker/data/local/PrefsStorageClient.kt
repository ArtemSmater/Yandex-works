package com.example.playlistmaker.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.google.gson.Gson
import java.lang.reflect.Type

class PrefsStorageClient<T>(
    context: Context,
    private val dataKey: String,
    private val type: Type
) : StorageClient<T> {

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCE_NAME,
        MODE_PRIVATE
    )

    private val gson = Gson()

    override fun saveData(data: T) {
        sharedPreferences.edit { putString(dataKey, gson.toJson(data, type)) }
    }

    override fun getData(): T? {
        val data = sharedPreferences.getString(dataKey, null)
        return if (data == null) {
            null
        } else {
            gson.fromJson(data, type)
        }
    }

    companion object {
        const val SHARED_PREFERENCE_NAME = "shared_preference_name"
        const val SHARED_KEY_THEME = "key_of_theme_value"
        const val SHARED_KEY_CACHE = "key_of_track_cache_list"
    }
}