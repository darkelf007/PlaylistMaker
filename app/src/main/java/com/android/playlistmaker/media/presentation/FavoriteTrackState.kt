package com.android.playlistmaker.media.presentation

import com.android.playlistmaker.media.domain.models.FavoriteTrack

data class FavouriteTrackState(
    val libraryTracks: List<FavoriteTrack>,
    val isLoading: Boolean
)
