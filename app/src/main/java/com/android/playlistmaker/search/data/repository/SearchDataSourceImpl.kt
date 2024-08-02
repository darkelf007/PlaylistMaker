package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.api.iTunesAPI
import com.android.playlistmaker.search.data.model.TrackResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchDataSourceImpl : SearchDataSource {
    private val ITunesApiBaseUrl = "https://itunes.apple.com"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITunesApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService: iTunesAPI = retrofit.create(iTunesAPI::class.java)

    override suspend fun search(query: String): TrackResponseData {
        return withContext(Dispatchers.IO) {
            val response = iTunesService.search(query).execute()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Empty response body")
            } else {
                throw Exception("Search failed with error: ${response.code()} - ${response.message()}")
            }
        }
    }
}
