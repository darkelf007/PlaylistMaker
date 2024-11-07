package com.android.playlistmaker.favorites_tracks.presentation

import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack


data class FavouriteTrackState(
    val libraryTracks: List<FavoriteTrack>,
    val isLoading: Boolean
)
