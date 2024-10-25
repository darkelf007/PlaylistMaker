package com.android.playlistmaker.player.domain.models

const val COVER_ARTWORK = "512x512bb.jpg"

data class PlayerTrack(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertionTimeStamp: Long

) {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', COVER_ARTWORK)
}