package com.android.playlistmaker.search.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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