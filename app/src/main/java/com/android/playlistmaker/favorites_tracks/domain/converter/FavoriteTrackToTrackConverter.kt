package com.android.playlistmaker.favorites_tracks.domain.converter

import com.android.playlistmaker.favorites_tracks.data.dto.FavoriteTrackDto
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack


class FavoriteTrackToTrackConverter {
    fun mapList(dtoList: List<FavoriteTrackDto>): List<FavoriteTrack> {
        return dtoList.map { dto ->
            FavoriteTrack(
                trackId = dto.trackId,
                trackName = dto.trackName,
                artistName = dto.artistName,
                trackTimeMillis = dto.trackTimeMillis,
                artworkUrl100 = dto.artworkUrl100,
                collectionName = dto.collectionName,
                releaseDate = dto.releaseDate,
                primaryGenreName = dto.primaryGenreName,
                country = dto.country,
                previewUrl = dto.previewUrl
            )
        }
    }
}
