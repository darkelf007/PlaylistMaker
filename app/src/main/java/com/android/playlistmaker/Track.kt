package com.android.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: Int, // Продолжительность трека
    @SerializedName("artworkUrl100") val artworkUrl100: String,  // Ссылка на изображение обложки
)
