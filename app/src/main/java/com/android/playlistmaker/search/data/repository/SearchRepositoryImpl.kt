package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.api.iTunesAPI
import com.android.playlistmaker.search.data.model.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepositoryImpl(private val iTunesService: iTunesAPI) {

    fun search(query: String, callback: (Result<TrackResponse>) -> Unit) {
        iTunesService.search(query).enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Exception("No results found")))
                } else {
                    callback(Result.failure(Exception("Failed to fetch results")))
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }
}