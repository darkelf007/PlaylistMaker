package com.android.playlistmaker.search.data.api

import com.android.playlistmaker.search.data.model.TrackResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesAPI {
    @GET("/search?entity=song")
    suspend fun search(@Query(value = "term", encoded = true) text: String, @Query("limit") limit: Int = 30): TrackResponseData
}
