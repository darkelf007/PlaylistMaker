package com.android.playlistmaker.favorites_tracks.data.converter

import com.android.playlistmaker.db.entity.DBTrackEntity
import com.android.playlistmaker.favorites_tracks.data.dto.FavoriteTrackDto


class FavoriteTrackDbConverter {

    fun map(favoriteTrackDto: FavoriteTrackDto): DBTrackEntity {
        return DBTrackEntity(
            trackId = favoriteTrackDto.trackId,
            trackName = favoriteTrackDto.trackName,
            artistName = favoriteTrackDto.artistName,
            trackTimeMillis = favoriteTrackDto.trackTimeMillis,
            artworkUrl100 = favoriteTrackDto.artworkUrl100,
            collectionName = favoriteTrackDto.collectionName,
            releaseDate = favoriteTrackDto.releaseDate,
            primaryGenreName = favoriteTrackDto.primaryGenreName,
            country = favoriteTrackDto.country,
            insertionTimeStamp = favoriteTrackDto.insertionTimeStamp,
            previewUrl = favoriteTrackDto.previewUrl

        )
    }

    fun map(dbTrackEntity: DBTrackEntity): FavoriteTrackDto {
        return FavoriteTrackDto(
            trackId = dbTrackEntity.trackId,
            trackName = dbTrackEntity.trackName,
            artistName = dbTrackEntity.artistName,
            trackTimeMillis = dbTrackEntity.trackTimeMillis,
            artworkUrl100 = dbTrackEntity.artworkUrl100,
            collectionName = dbTrackEntity.collectionName,
            releaseDate = dbTrackEntity.releaseDate,
            primaryGenreName = dbTrackEntity.primaryGenreName,
            country = dbTrackEntity.country,
            previewUrl = dbTrackEntity.previewUrl,
            insertionTimeStamp = dbTrackEntity.insertionTimeStamp
        )
    }

}
