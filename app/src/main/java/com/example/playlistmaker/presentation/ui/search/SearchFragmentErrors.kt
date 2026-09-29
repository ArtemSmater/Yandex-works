package com.example.playlistmaker.presentation.ui.search

sealed class SearchFragmentErrors(
    val tvVisible: Boolean,
    val ivVisible: Boolean,
    val bnVisible: Boolean,
) {

    object HideSearchErrors : SearchFragmentErrors(false, false, false)

    class EmptyResponse : SearchFragmentErrors(true, true, false)

    class InternetConnection : SearchFragmentErrors(true, true, true)
}