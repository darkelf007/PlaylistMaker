package com.android.playlistmaker.media.domain.impl

import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseInteractor
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistMediaDatabaseInteractorImpl(
    private val playlistMediaDatabaseRepository: PlaylistMediaDatabaseRepository
) : PlaylistMediaDatabaseInteractor {
    override suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>> {
        return playlistMediaDatabaseRepository.getPlaylistsFromDatabase()
    }
}