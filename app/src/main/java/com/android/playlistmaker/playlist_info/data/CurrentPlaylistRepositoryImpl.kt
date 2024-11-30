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

        val playlist = playlistDao.getPlaylistById(playlistId)?.mapToPlaylist()
        if (playlist != null) {
            val listOfTracks = convertStringToList(playlist.listOfTracksId).filter { it != trackId }
            val updatedListString = convertListToString(listOfTracks)
            val updatedPlaylist = playlist.copy(
                listOfTracksId = updatedListString,
                amountOfTracks = playlist.amountOfTracks - 1
            )
            playlistDao.updatePlaylist(updatedPlaylist.mapToPlaylistEntity())
        }
    }

    private fun convertStringToList(string: String): List<Int> {
        return if (string.isEmpty()) emptyList() else string.split(",").mapNotNull { it.toIntOrNull() }
    }

    private fun convertListToString(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }
}
