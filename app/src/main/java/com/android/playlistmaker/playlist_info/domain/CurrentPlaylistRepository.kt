package com.android.playlistmaker.playlist_info.domain

import com.android.playlistmaker.new_playlist.domain.models.Playlist

interface CurrentPlaylistRepository {
    suspend fun getPlaylistById(playlistId: Long): Playlist?
}