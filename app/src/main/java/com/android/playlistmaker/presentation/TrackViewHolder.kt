package com.android.playlistmaker.presentation

import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.domain.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackViewHolder(itemView: View, private val resources: Resources) : RecyclerView.ViewHolder(itemView) {
    val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
    private val trackNameView: TextView
    private val artistNameView: TextView
    val trackTimeView: TextView
    val artworkUrl100View: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.song_name)
        artistNameView = itemView.findViewById(R.id.artist_name)
        trackTimeView = itemView.findViewById(R.id.song_length)
        artworkUrl100View = itemView.findViewById(R.id.album_picture)
    }

    fun bind(track: Track) {
        trackNameView.text = track.trackName
        artistNameView.text = track.artistName
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(pixels))
            .into(artworkUrl100View)
    }
}