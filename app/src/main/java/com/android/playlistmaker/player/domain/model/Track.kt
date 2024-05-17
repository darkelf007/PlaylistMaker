package com.android.playlistmaker.player.domain.model

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)