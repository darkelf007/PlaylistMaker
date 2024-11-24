package com.android.playlistmaker.player.data.converter

import com.android.playlistmaker.db.entity.DBTrackEntity
import com.android.playlistmaker.player.data.dto.PlayerTrackDto

class PlayerTrackDbConverter {

    fun map(playerTrackDto: PlayerTrackDto): DBTrackEntity {
        return DBTrackEntity(
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

    fun map(DBTrackEntity: DBTrackEntity): PlayerTrackDto {
        return PlayerTrackDto(
            trackId = DBTrackEntity.trackId,
            trackName = DBTrackEntity.trackName,
            artistName = DBTrackEntity.artistName,
            trackTimeMillis = DBTrackEntity.trackTimeMillis,
            artworkUrl100 = DBTrackEntity.artworkUrl100,
            artworkUrl60 = DBTrackEntity.artworkUrl60,
            collectionName = DBTrackEntity.collectionName,
            releaseDate = DBTrackEntity.releaseDate,
            primaryGenreName = DBTrackEntity.primaryGenreName,
            country = DBTrackEntity.country,
            previewUrl = DBTrackEntity.previewUrl,
            insertionTimeStamp = DBTrackEntity.insertionTimeStamp
        )
    }

}
