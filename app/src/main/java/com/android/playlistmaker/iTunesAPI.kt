package com.android.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}