package com.android.playlistmaker.new_playlist.domain.db

import com.android.playlistmaker.new_playlist.domain.models.Playlist

interface PlaylistDatabaseRepository {
    suspend fun insertPlaylistToDatabase(playlist: Playlist)

}