package com.android.playlistmaker.new_playlist.domain.db

import android.net.Uri
import com.android.playlistmaker.new_playlist.domain.models.Playlist

interface PlaylistInteractor {
    suspend fun addPlaylist(name: String, description: String, uriOfImage: Uri?)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: Long): Playlist?
}