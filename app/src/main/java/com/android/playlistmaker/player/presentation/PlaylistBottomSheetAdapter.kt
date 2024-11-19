package com.android.playlistmaker.player.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class PlaylistBottomSheetAdapter(
    private val context: Context,
    private val clickListener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistBottomSheetHolder>() {

    private val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomSheetHolder {
        return PlaylistBottomSheetHolder.create(parent, context)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { clickListener.invoke(playlist) }
    }

    override fun getItemCount(): Int = playlists.size

    fun setPlaylists(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}


class PlaylistBottomSheetHolder(
    parent: ViewGroup,
    private val context: Context
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_sheet_view, parent, false)
) {

    private val playlistBottomSheetImageView: ImageView =
        itemView.findViewById(R.id.playlist_bottom_sheet_imageview)
    private val playlistNameTextView: TextView =
        itemView.findViewById(R.id.playlist_name_textview)
    private val playlistTrackAmountTextView: TextView =
        itemView.findViewById(R.id.playlist_track_amount_textview)

    fun bind(playlist: Playlist) {
        if (playlist.imageUri != null) {
            Glide.with(context)
                .load(playlist.imageUri)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .apply(RequestOptions().transform(RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.dp_8))))
                .into(playlistBottomSheetImageView)
        } else {
            playlistBottomSheetImageView.setImageResource(R.drawable.placeholder)
        }

        playlistNameTextView.text = playlist.name
        playlistTrackAmountTextView.text = pluralizeWord(playlist.amountOfTracks, "трек")
    }

    private fun pluralizeWord(number: Int, word: String): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number $word"
            number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20) -> "$number $word${if (word.endsWith('а')) "и" else "а"}"
            else -> "$number $word${if (word.endsWith('а')) "" else "ов"}"
        }
    }

    companion object {
        fun create(parent: ViewGroup, context: Context): PlaylistBottomSheetHolder {
            return PlaylistBottomSheetHolder(parent, context)
        }
    }
}