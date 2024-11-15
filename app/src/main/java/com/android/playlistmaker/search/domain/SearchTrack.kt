package com.android.playlistmaker.search.domain

import android.os.Parcelable
import com.android.playlistmaker.player.domain.models.PlayerTrack
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

const val COVER_ARTWORK = "512x512bb.jpg"

@Parcelize
data class SearchTrack(
    @SerializedName("trackId") val trackId: Int,
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    val insertionTimeStamp: Long = 0L
) : Parcelable

fun SearchTrack.mapToPlayerTrack(): PlayerTrack {
    return PlayerTrack(
        trackName,
        artistName,
        trackTimeMillis,
        artworkUrl100,
        trackId,
        collectionName,
        releaseDate,
        primaryGenreName,
        country,
        previewUrl,
        insertionTimeStamp
    )
}