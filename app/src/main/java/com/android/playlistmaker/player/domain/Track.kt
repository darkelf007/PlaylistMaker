package com.android.playlistmaker.player.domain

import com.google.gson.annotations.SerializedName
const val COVER_ARTWORK = "512x512bb.jpg"

data class Track(
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    val trackId: String,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?

) {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', COVER_ARTWORK)
}