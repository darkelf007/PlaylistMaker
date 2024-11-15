package com.android.playlistmaker.media.data.repository

import com.android.playlistmaker.db.PlaylistDatabase
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class PlaylistMediaDatabaseRepositoryImpl(private val playlistDatabase: PlaylistDatabase) :
    PlaylistMediaDatabaseRepository {
    override suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>> = flow {
        val playlistEntityList = playlistDatabase.playlistDao().getPlaylists()
        emit(playlistEntityList.map { playlistEntity -> playlistEntity.mapToPlaylist() })
    }


}