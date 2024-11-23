package com.android.playlistmaker.new_playlist.domain.db

import com.android.playlistmaker.new_playlist.domain.models.Playlist

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: Long): Playlist?
}