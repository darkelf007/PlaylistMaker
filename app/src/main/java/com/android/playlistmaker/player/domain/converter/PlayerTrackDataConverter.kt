package com.android.playlistmaker.player.domain.converter

import com.android.playlistmaker.player.data.dto.PlayerTrackDto
import com.android.playlistmaker.player.domain.models.PlayerTrack

class PlayerTrackDataConverter {
    fun map(playerTrack: PlayerTrack, insertionTimeStamp: Long? = null): PlayerTrackDto {
        return PlayerTrackDto(
            trackId = playerTrack.trackId,
            trackName = playerTrack.trackName,
            artistName = playerTrack.artistName,
            trackTimeMillis = playerTrack.trackTimeMillis,
            artworkUrl100 = playerTrack.artworkUrl100,
            artworkUrl60 = playerTrack.artworkUrl60,
            collectionName = playerTrack.collectionName,
            releaseDate = playerTrack.releaseDate,
            primaryGenreName = playerTrack.primaryGenreName,
            country = playerTrack.country,
            previewUrl = playerTrack.previewUrl,
            insertionTimeStamp = playerTrack.insertionTimeStamp
        )
    }

    fun map(playerTrackDto: PlayerTrackDto): PlayerTrack {
        return PlayerTrack(
            trackId = playerTrackDto.trackId,
            trackName = playerTrackDto.trackName,
            artistName = playerTrackDto.artistName,
            trackTimeMillis = playerTrackDto.trackTimeMillis,
            artworkUrl100 = playerTrackDto.artworkUrl100,
            artworkUrl60 = playerTrackDto.artworkUrl60,
            collectionName = playerTrackDto.collectionName,
            releaseDate = playerTrackDto.releaseDate,
            primaryGenreName = playerTrackDto.primaryGenreName,
            country = playerTrackDto.country,
            previewUrl = playerTrackDto.previewUrl,
            insertionTimeStamp = playerTrackDto.insertionTimeStamp
        )
    }
}