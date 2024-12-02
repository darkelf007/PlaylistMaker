package com.android.playlistmaker.playlist_info.data

import android.content.Context
import android.os.Environment
import com.android.playlistmaker.db.dao.PlaylistDao
import com.android.playlistmaker.db.dao.PlaylistTrackDao
import com.android.playlistmaker.db.dao.TrackDao
import com.android.playlistmaker.db.entity.mapToSearchTrack
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylistEntity
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository
import com.android.playlistmaker.search.domain.SearchTrack
import java.io.File

class CurrentPlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackDao: TrackDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val appContext: Context
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
        return if (string.isEmpty()) emptyList() else string.split(",")
            .mapNotNull { it.toIntOrNull() }
    }

    private fun convertListToString(list: List<Int>): String {
        return list.joinToString(separator = ",")
    }

    override fun getPlaylistCoverPath(filePath: String): String? {
        if (filePath.isBlank()) {
            return null
        }

        val file = if (filePath.startsWith("myalbum/")) {
            File(appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filePath)
        } else {
            File(
                appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "myalbum/$filePath"
            )
        }
        return if (file.exists()) file.absolutePath else null
    }

    override suspend fun isTrackInOtherPlaylists(trackId: Int): Boolean {
        val usageCount = playlistTrackDao.getTrackUsageCount(trackId)
        return usageCount > 1
    }

    override suspend fun deleteTrackCompletely(trackId: Int) {
        val trackEntity = trackDao.getTrackById(trackId)
        if (trackEntity != null) {
            trackDao.deleteTrack(trackEntity)
        }
    }
}
