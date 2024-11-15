package com.android.playlistmaker.new_playlist.domain.impl


import com.android.playlistmaker.new_playlist.domain.db.PlaylistDatabaseInteractor
import com.android.playlistmaker.new_playlist.domain.db.PlaylistDatabaseRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist

class PlaylistDatabaseInteractorImpl(
    private val playlistDatabaseRepository: PlaylistDatabaseRepository
) : PlaylistDatabaseInteractor {
    override suspend fun insertPlaylistToDatabase(playlist: Playlist) {
        playlistDatabaseRepository.insertPlaylistToDatabase(playlist)
    }

}