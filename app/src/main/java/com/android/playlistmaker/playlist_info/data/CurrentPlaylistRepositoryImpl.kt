package com.android.playlistmaker.playlist_info.data

import android.util.Log
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
        Log.d("Repository", "Fetching tracks with IDs: $ids")

        val trackEntities = playlistTrackDao.getTracksByListIds(ids)


        Log.d("Repository", "Fetched ${trackEntities.size} track(s) from database")


        trackEntities.forEach { track ->
            Log.d(
                "Repository",
                "Track entity: ID=${track.trackId}, Name=${track.trackName}, Time=${track.trackTime}"
            )
        }

        val searchTracks = trackEntities.map { it.mapToSearchTrack() }


        searchTracks.forEach { track ->
            Log.d(
                "Repository",
                "Mapped track: ID=${track.trackId}, Name=${track.trackName}, TimeMillis=${track.trackTimeMillis}"
            )
        }

        return searchTracks
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        Log.d("Repository", "Fetching playlist with ID: $playlistId")

        val playlistEntity = playlistDao.getPlaylistById(playlistId)
        if (playlistEntity == null) {
            Log.w("Repository", "Playlist with ID=$playlistId not found")
        } else {
            Log.d("Repository", "Playlist entity: $playlistEntity")
        }

        val playlist = playlistEntity?.mapToPlaylist()
        if (playlist != null) {
            Log.d(
                "Repository",
                "Mapped playlist: ID=${playlist.id}, Name=${playlist.name}, TrackCount=${playlist.amountOfTracks}, TrackTime=${playlist.trackTime}"
            )
        }

        return playlist
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(playlist.mapToPlaylistEntity())
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {

        playlistTrackDao.deleteTrackById(trackId)


        val playlist = playlistDao.getPlaylistById(playlistId)?.mapToPlaylist()
        playlist?.let {
            val updatedTrackIds = it.listOfTracksId.split(",")
                .filter { id -> id.toIntOrNull() != trackId }
                .joinToString(",")

            val updatedPlaylist = it.copy(
                listOfTracksId = updatedTrackIds,
                amountOfTracks = if (updatedTrackIds.isEmpty()) 0 else updatedTrackIds.split(",").size
            )
            playlistDao.updatePlaylist(updatedPlaylist.mapToPlaylistEntity())
        }
    }
}