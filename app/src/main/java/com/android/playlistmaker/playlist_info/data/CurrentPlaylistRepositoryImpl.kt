package com.android.playlistmaker.playlist_info.data

import com.android.playlistmaker.db.PlaylistDatabase
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository

class CurrentPlaylistRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase
) : CurrentPlaylistRepository {
    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        val playlistEntity = playlistDatabase.playlistDao().getPlaylistById(playlistId)
        return playlistEntity?.mapToPlaylist()
    }
}