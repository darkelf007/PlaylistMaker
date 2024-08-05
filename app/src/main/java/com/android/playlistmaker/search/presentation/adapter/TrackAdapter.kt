package com.android.playlistmaker.search.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.search.domain.SearchTrack
import com.android.playlistmaker.search.presentation.viewholder.TrackViewHolder


class TrackAdapter(private var searchTracks: List<SearchTrack>, private val resources: Resources) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((SearchTrack) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_track_card, parent, false)
        return TrackViewHolder(view, resources)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(searchTracks[position])
        holder.itemView.setOnClickListener { itemClickListener?.invoke(searchTracks[position]) }
    }

    override fun getItemCount() = searchTracks.size

    fun updateTracks(newSearchTracks: List<SearchTrack>) {
        searchTracks = newSearchTracks
        notifyDataSetChanged()
    }
}