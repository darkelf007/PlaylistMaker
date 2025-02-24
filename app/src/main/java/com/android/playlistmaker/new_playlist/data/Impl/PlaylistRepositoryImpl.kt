package com.android.playlistmaker.new_playlist.data.Impl

import com.android.playlistmaker.db.AppDatabase
import com.android.playlistmaker.new_playlist.domain.db.PlaylistRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylistEntity

class PlaylistRepositoryImpl(
    private val playlistDatabase: AppDatabase
) : PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().insertPlaylist(playlist.mapToPlaylistEntity())
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDatabase.playlistDao().updatePlaylist(playlist.mapToPlaylistEntity())
    }

    override suspend fun getPlaylistById(id: Long): Playlist? {
        val playlistEntity = playlistDatabase.playlistDao().getPlaylistById(id)
        return playlistEntity?.mapToPlaylist()
    }
}