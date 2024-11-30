package com.android.playlistmaker.playlist_info.data

import com.android.playlistmaker.db.dao.PlaylistDao
import com.android.playlistmaker.db.dao.PlaylistTrackDao
import com.android.playlistmaker.db.entity.mapToSearchTrack
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylistEntity
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository
import com.android.playlistmaker.search.domain.SearchTrack

class CurrentPlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao
) : CurrentPlaylistRepository {

    override suspend fun getTracksByIds(ids: List<Int>): List<SearchTrack> {
        val trackEntities = playlistTrackDao.getTracksByListIds(ids)
        return trackEntities.map { it.mapToSearchTrack() }
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        val playlistEntity = playlistDao.getPlaylistById(playlistId)
        return playlistEntity?.mapToPlaylist()
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.mapToPlaylistEntity())
    }

    override suspend fun getTracksByPlaylistId(playlistId: Long): List<SearchTrack> {
        val trackEntities = playlistTrackDao.getTracksByPlaylistId(playlistId)
        return trackEntities.map { it.mapToSearchTrack() }
    }


    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        playlistTrackDao.deleteTrackFromPlaylist(playlistId, trackId)
        val trackCount = playlistTrackDao.getTrackCountInPlaylist(playlistId)
        val playlist = playlistDao.getPlaylistById(playlistId)?.mapToPlaylist()
        playlist?.let {
            val updatedPlaylist = it.copy(amountOfTracks = trackCount)
            playlistDao.updatePlaylist(updatedPlaylist.mapToPlaylistEntity())
        }
    }
}
