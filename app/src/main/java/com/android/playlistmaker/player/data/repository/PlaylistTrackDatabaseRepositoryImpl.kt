package com.android.playlistmaker.player.data.repository

import com.android.playlistmaker.db.PlaylistTrackDatabase
import com.android.playlistmaker.db.entity.PlaylistTrackEntity
import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseRepository
import com.android.playlistmaker.search.domain.SearchTrack

class PlaylistTrackDatabaseRepositoryImpl(
    private val playlistTrackDatabase: PlaylistTrackDatabase
) : PlaylistTrackDatabaseRepository {

    override suspend fun insertTrackToPlaylistTrackDatabase(track: SearchTrack) {
        val playlistTrackEntity = PlaylistTrackEntity(
            trackId = track.trackId
                ?: throw IllegalArgumentException("trackId не может быть null"), // Обработка null
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = formatTrackTime(track.trackTimeMillis),
            artworkUrl = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            insertTimeStamp = System.currentTimeMillis()
        )
        playlistTrackDatabase.playlistTrackDao().insertTrack(playlistTrackEntity)
    }

    private fun formatTrackTime(trackTimeMillis: Int): String {
        val seconds = (trackTimeMillis / 1000) % 60
        val minutes = (trackTimeMillis / (1000 * 60) % 60)
        return String.format("%02d:%02d", minutes, seconds)
    }
}


