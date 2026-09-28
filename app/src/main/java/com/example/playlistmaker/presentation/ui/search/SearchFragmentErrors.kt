package com.example.playlistmaker.presentation.ui.search

import android.graphics.drawable.Drawable

sealed class SearchFragmentErrors(
    val tvVisible: Boolean,
    val ivVisible: Boolean,
    val bnVisible: Boolean,
    val errorMsg: String?,
    val errorImg: Drawable?
) {

    object HideSearchErrors : SearchFragmentErrors(false, false, false, null, null)

    class EmptyResponse(errorMsg: String, errorImg: Drawable
    ) : SearchFragmentErrors(true, true, false, errorMsg, errorImg)

    class InternetConnection(errorMsg: String, errorImg: Drawable) :
        SearchFragmentErrors(true, true, true, errorMsg, errorImg)
}