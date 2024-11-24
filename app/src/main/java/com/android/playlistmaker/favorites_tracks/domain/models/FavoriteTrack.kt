package com.android.playlistmaker.favorites_tracks.domain.models

data class FavoriteTrack(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)