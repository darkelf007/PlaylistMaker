package com.android.playlistmaker.media.data.converter

import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import com.android.playlistmaker.search.domain.SearchTrack

class FavoriteTrackToSearchTrackConverter {
    fun map(favoriteTrack: FavoriteTrack): SearchTrack {
        return SearchTrack(
            trackId = favoriteTrack.trackId,
            trackName = favoriteTrack.trackName,
            artistName = favoriteTrack.artistName,
            trackTimeMillis = favoriteTrack.trackTimeMillis,
            artworkUrl100 = favoriteTrack.artworkUrl100,
            collectionName = favoriteTrack.collectionName,
            releaseDate = favoriteTrack.releaseDate,
            primaryGenreName = favoriteTrack.primaryGenreName,
            country = favoriteTrack.country,
            previewUrl = favoriteTrack.previewUrl
        )
    }
}