package com.android.playlistmaker.new_playlist.data.repository

import com.android.playlistmaker.db.PlaylistDatabase
import com.android.playlistmaker.new_playlist.domain.db.PlaylistDatabaseRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylistEntity

class PlaylistDatabaseRepositoryImpl(private val playlistDatabase: PlaylistDatabase) :
    PlaylistDatabaseRepository {
    override suspend fun insertPlaylistToDatabase(playlist: Playlist) {
        playlistDatabase.playlistDao().insertPlaylist(playlist.mapToPlaylistEntity())
    }

}