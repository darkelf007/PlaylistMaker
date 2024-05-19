package com.android.playlistmaker.search.data.api

import com.android.playlistmaker.search.data.model.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesAPI {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>
}