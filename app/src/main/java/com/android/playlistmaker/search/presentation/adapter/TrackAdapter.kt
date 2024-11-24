package com.android.playlistmaker.search.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.search.domain.SearchTrack
import com.android.playlistmaker.search.presentation.viewholder.TrackViewHolder


class TrackAdapter(private var searchTracks: List<SearchTrack>, private val resources: Resources) :
    RecyclerView.Adapter<TrackViewHolder>() {


    var itemClickListener: ((SearchTrack) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_track_card, parent, false)
        return TrackViewHolder(view, resources)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = searchTracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(track)
        }
    }

    override fun getItemCount() = searchTracks.size

    fun updateTracks(newSearchTracks: List<SearchTrack>) {
        val diffCallback = TrackDiffCallback(tracks = searchTracks, newTracks = newSearchTracks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        searchTracks = newSearchTracks
        diffResult.dispatchUpdatesTo(this)
    }

    class TrackDiffCallback(
        private val tracks: List<SearchTrack>,
        private val newTracks: List<SearchTrack>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = tracks.size
        override fun getNewListSize() = newTracks.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return tracks[oldItemPosition].trackId == newTracks[newItemPosition].trackId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return tracks[oldItemPosition] == newTracks[newItemPosition]
        }
    }
}