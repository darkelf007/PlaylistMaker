package com.android.playlistmaker.favorites_tracks.domain.converter

import com.android.playlistmaker.favorites_tracks.data.dto.FavoriteTrackDto
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import com.android.playlistmaker.search.domain.SearchTrack


class FavoriteTrackToTrackConverter {
    fun mapList(dtoList: List<FavoriteTrackDto>): List<FavoriteTrack> {
        return dtoList.map { dto ->
            FavoriteTrack(
                trackId = dto.trackId,
                trackName = dto.trackName,
                artistName = dto.artistName,
                trackTimeMillis = dto.trackTimeMillis,
                artworkUrl100 = dto.artworkUrl100,
                artworkUrl60 = dto.artworkUrl60,
                collectionName = dto.collectionName,
                releaseDate = dto.releaseDate,
                primaryGenreName = dto.primaryGenreName,
                country = dto.country,
                previewUrl = dto.previewUrl
            )
        }
    }

    fun map(favoriteTrack: FavoriteTrack): SearchTrack {
        return SearchTrack(
            trackId = favoriteTrack.trackId,
            trackName = favoriteTrack.trackName,
            artistName = favoriteTrack.artistName,
            trackTimeMillis = favoriteTrack.trackTimeMillis,
            artworkUrl100 = favoriteTrack.artworkUrl100,
            artworkUrl60 = favoriteTrack.artworkUrl60,
            collectionName = favoriteTrack.collectionName,
            releaseDate = favoriteTrack.releaseDate,
            primaryGenreName = favoriteTrack.primaryGenreName,
            country = favoriteTrack.country,
            previewUrl = favoriteTrack.previewUrl
        )
    }

}
