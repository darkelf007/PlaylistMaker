package com.android.playlistmaker.player.presentation

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.io.File


class PlaylistBottomSheetAdapter(val context: Context, val clickListener: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistBottomSheetHolder>() {

    val playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistBottomSheetHolder {
        return PlaylistBottomSheetHolder(parent, context)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.invoke(playlists[position]) }
    }

}

class PlaylistBottomSheetHolder(parent: ViewGroup, val context: Context) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_sheet_view, parent, false)
) {

    private val playlistBottomSheetImageView =
        itemView.findViewById<ImageView>(R.id.playlist_bottom_sheet_imageview)
    private val playlistNameTextView = itemView.findViewById<TextView>(R.id.playlist_name_textview)
    private val playlistTrackAmountTextView =
        itemView.findViewById<TextView>(R.id.playlist_track_amount_textview)

    fun bind(playlist: Playlist) {
        if (playlist.filePath.isNotEmpty()) {
            val uri = getUriOfImageFromStorage(playlist.filePath)
            Glide.with(context)
                .load(uri)
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

    private fun getUriOfImageFromStorage(fileName: String): Uri? {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, fileName)
        return if (file.exists()) file.toUri() else null
    }

    private fun pluralizeWord(number: Int, word: String): String {
        return when {
            number % 10 == 1 && number % 100 != 11 -> "$number $word"
            number % 10 in 2..4 && (number % 100 < 10 || number % 100 >= 20) -> "$number $word${
                if (word.endsWith(
                        'а'
                    )
                ) "и" else "а"
            }"

            else -> "$number $word${if (word.endsWith('а')) "" else "ов"}"
        }
    }

}