package com.android.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: Int, // Продолжительность трека
    @SerializedName("artworkUrl100") val artworkUrl100: String,  // Ссылка на изображение обложки
    val trackId: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
){fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")}
