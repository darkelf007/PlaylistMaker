package com.android.playlistmaker.favorites_tracks.domain.converter

import com.android.playlistmaker.favorites_tracks.data.dto.FavoriteTrackDto
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack


class FavoriteTrackDataConverter {

    fun map(libraryTrack: FavoriteTrack): FavoriteTrackDto {
        return FavoriteTrackDto(
            trackId = libraryTrack.trackId,
            trackName = libraryTrack.trackName,
            artistName = libraryTrack.artistName,
            trackTimeMillis = libraryTrack.trackTimeMillis,
            artworkUrl100 = libraryTrack.artworkUrl100,
            artworkUrl60 = libraryTrack.artworkUrl60,
            collectionName = libraryTrack.collectionName,
            releaseDate = libraryTrack.releaseDate,
            primaryGenreName = libraryTrack.primaryGenreName,
            country = libraryTrack.country,
            previewUrl = libraryTrack.previewUrl,
            insertionTimeStamp = System.currentTimeMillis()
        )
    }

    fun map(libraryTrackDto: FavoriteTrackDto): FavoriteTrack {
        return FavoriteTrack(
            trackId = libraryTrackDto.trackId,
            trackName = libraryTrackDto.trackName,
            artistName = libraryTrackDto.artistName,
            trackTimeMillis = libraryTrackDto.trackTimeMillis,
            artworkUrl100 = libraryTrackDto.artworkUrl100,
            artworkUrl60 = libraryTrackDto.artworkUrl60,
            collectionName = libraryTrackDto.collectionName,
            releaseDate = libraryTrackDto.releaseDate,
            primaryGenreName = libraryTrackDto.primaryGenreName,
            country = libraryTrackDto.country,
            previewUrl = libraryTrackDto.previewUrl
        )
    }


}
