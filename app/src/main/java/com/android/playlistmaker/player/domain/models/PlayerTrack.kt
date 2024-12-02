package com.android.playlistmaker.player.domain.models

import com.android.playlistmaker.search.domain.COVER_ARTWORK


data class PlayerTrack(
    val trackName: String?,
    val artistName: String?,
    val trackTimeMillis: Int,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val trackId: Int,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertionTimeStamp: Long? = null

) {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', COVER_ARTWORK)
}