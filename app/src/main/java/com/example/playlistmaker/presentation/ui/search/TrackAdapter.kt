package com.example.playlistmaker.presentation.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.utils.TrackDiffCallback
import com.example.playlistmaker.presentation.utils.Transform

class TrackAdapter : ListAdapter<Track, TrackAdapter.TrackViewHolder>(TrackDiffCallback()) {
    var onTrackClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val binding = TrackViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onTrackClickListener?.invoke(item)
        }
    }

    class TrackViewHolder(private val binding: TrackViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Track) {
            with(binding) {
                tvTrackName.text = model.trackName
                tvTrackAuthor.text = model.artistName
                tvDuration.text = Transform.millsToMins(model.trackTimeMillis)

                val multiTransformation = MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(Transform.dpToPx(2f, itemView.context))
                )

                Glide.with(itemView)
                    .load(model.artworkUrl100)
                    .apply(RequestOptions.bitmapTransform(multiTransformation))
                    .placeholder(R.drawable.placeholder)
                    .into(ivTrackIcon)
            }
        }
    }
}