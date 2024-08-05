package com.android.playlistmaker.search.data.model

import com.google.gson.annotations.SerializedName

data class TrackDataModel(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String

)

