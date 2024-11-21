package com.android.playlistmaker.media.domain.db

import com.android.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistMediaDatabaseRepository {
    suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>>
}