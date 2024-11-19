package com.android.playlistmaker.media.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class PlaylistAdapter(
    private val context: Context,
    private val clickListener: (Playlist) -> Unit
) : ListAdapter<Playlist, PlaylistHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        return PlaylistHolder.create(parent, context)
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        val playlist = getItem(position)
        holder.bind(playlist)
        holder.itemView.setOnClickListener { clickListener.invoke(playlist) }
    }

    class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }
}

class PlaylistHolder(
    parent: ViewGroup,
    private val context: Context
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
) {

    private val playlistImageView: ImageView = itemView.findViewById(R.id.playlist_imageview)
    private val nameOfPlaylistTextView: TextView =
        itemView.findViewById(R.id.name_of_playlist_textview)
    private val numberOfTracksTextView: TextView =
        itemView.findViewById(R.id.number_of_tracks_textview)

    fun bind(playlist: Playlist) {
        if (playlist.imageUri != null) {
            Glide.with(context)
                .load(playlist.imageUri)
                .placeholder(R.drawable.placeholder_playlist)
                .error(R.drawable.placeholder_playlist)
                .apply(RequestOptions().transform(RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.dp_8))))
                .into(playlistImageView)
        } else {
            playlistImageView.setImageResource(R.drawable.placeholder_playlist)
        }

        nameOfPlaylistTextView.text = playlist.name
        numberOfTracksTextView.text = getTrackCountString(playlist.amountOfTracks)
    }

    private fun getTrackCountString(count: Int): String {
        return context.resources.getQuantityString(R.plurals.number_of_tracks, count, count)
    }

    companion object {
        fun create(parent: ViewGroup, context: Context): PlaylistHolder {
            return PlaylistHolder(parent, context)
        }
    }
}