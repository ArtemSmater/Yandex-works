package com.example.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.pojo.Track
import com.example.playlistmaker.utils.Transform


class TrackAdapter(val tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPoster = itemView.findViewById<ImageView>(R.id.ivTrackIcon)
        private val tvTrack = itemView.findViewById<TextView>(R.id.tvTrackName)
        private val tvAuthor = itemView.findViewById<TextView>(R.id.tvTrackAuthor)

        fun bind(model: Track) {
            tvTrack.text = model.trackName
            "${model.artistName} • ${model.trackTime}".also { tvAuthor.text = it }

            val multiTransformation = MultiTransformation(
                CenterCrop(),
                RoundedCorners(Transform.dpToPx(2f, itemView.context))
            )

            Glide.with(itemView)
                .load(model.artworkUrl100)
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .placeholder(R.drawable.baseline_music_note_24)
                .into(ivPoster)
        }
    }
}