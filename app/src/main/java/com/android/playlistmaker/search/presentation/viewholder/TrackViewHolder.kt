package com.android.playlistmaker.search.presentation.viewholder

import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.search.domain.SearchTrack
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackViewHolder(itemView: View, private val resources: Resources) :
    RecyclerView.ViewHolder(itemView) {
    private val pixels =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
    private val trackNameView: TextView = itemView.findViewById(R.id.song_name)
    private val artistNameView: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTimeView: TextView = itemView.findViewById(R.id.song_length)
    private val artworkUrl100View: ImageView = itemView.findViewById(R.id.album_picture)

    fun bind(searchTrack: SearchTrack) {
        trackNameView.text = searchTrack.trackName
        artistNameView.text = searchTrack.artistName
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(searchTrack.trackTimeMillis)
        Glide.with(itemView.context)
            .load(searchTrack.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(pixels))
            .into(artworkUrl100View)
    }
}