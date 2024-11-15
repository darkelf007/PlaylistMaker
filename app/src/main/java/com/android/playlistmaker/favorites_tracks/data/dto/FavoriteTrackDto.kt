package com.android.playlistmaker.favorites_tracks.data.dto

data class FavoriteTrackDto(
    val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertionTimeStamp: Long?
)