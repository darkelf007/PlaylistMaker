package com.android.playlistmaker.favorites_tracks.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteTrackAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<FavoriteTrackAdapter.TrackHolder>() {


    private val tracks = mutableListOf<FavoriteTrack>()

    fun setTracks(newTracks: List<FavoriteTrack>) {

        val diffCallback =
            FavoriteTrackDiffCallback(
                tracks,
                newTracks
            )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        tracks.clear()
        tracks.addAll(newTracks)
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface TrackClickListener {
        fun onTrackClick(libraryTrack: FavoriteTrack)
    }

    class TrackHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_track_card, parent, false)
    ) {
        private val artwork: ImageView = itemView.findViewById(R.id.album_picture)
        private val artistName: TextView = itemView.findViewById(R.id.artist_name)
        private val trackName: TextView = itemView.findViewById(R.id.song_name)
        private val trackTime: TextView = itemView.findViewById(R.id.song_length)

        fun bind(libraryTrack: FavoriteTrack) {

            val formattedTime = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(libraryTrack.trackTimeMillis.toLong())

            Glide.with(itemView.context)
                .load(libraryTrack.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .into(artwork)
            artistName.text = libraryTrack.artistName
            trackName.text = libraryTrack.trackName
            trackTime.text = formattedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        return TrackHolder(
            parent
        )
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        if (position < tracks.size) {
            val track = tracks[position]
            holder.bind(track)
            holder.itemView.setOnClickListener {
                clickListener.onTrackClick(track)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    class FavoriteTrackDiffCallback(
        private val oldList: List<FavoriteTrack>,
        private val newList: List<FavoriteTrack>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
