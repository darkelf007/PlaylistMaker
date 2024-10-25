package com.android.playlistmaker.media.domain.converter

import com.android.playlistmaker.media.data.dto.FavoriteTrackDto
import com.android.playlistmaker.media.domain.models.FavoriteTrack


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
