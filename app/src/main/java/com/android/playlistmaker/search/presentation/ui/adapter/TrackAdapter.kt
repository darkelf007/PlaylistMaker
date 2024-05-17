package com.android.playlistmaker.search.presentation.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.domain.model.Track
import com.android.playlistmaker.search.presentation.ui.viewholder.TrackViewHolder


class TrackAdapter(private val tracks: List<Track>, private val resources: Resources) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var itemClickListener: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_track_card, parent, false)
        return TrackViewHolder(view, resources)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { itemClickListener?.invoke(tracks[position]) }
    }

    override fun getItemCount() = tracks.size
}