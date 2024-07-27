package com.android.playlistmaker.player.dto

data class TrackViewData(
    val trackName: String,
    val artistName: String,
    val coverArtwork: String,
    val trackTime: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)