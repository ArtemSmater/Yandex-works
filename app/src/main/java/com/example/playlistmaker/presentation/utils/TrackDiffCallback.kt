package com.example.playlistmaker.presentation.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.entities.Track


class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(
        oldItem: Track,
        newItem: Track
    ): Boolean {
        return oldItem == newItem
    }
}