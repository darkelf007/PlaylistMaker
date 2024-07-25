package com.android.playlistmaker.search.data.repository

import android.util.Log
import com.android.playlistmaker.search.data.api.iTunesAPI
import com.android.playlistmaker.search.data.model.TrackResponse
import com.android.playlistmaker.search.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchRepositoryImpl: SearchRepository {
    private val ITunesApiBaseUrl = "https://itunes.apple.com"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ITunesApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService: iTunesAPI = retrofit.create(iTunesAPI::class.java)

    private val TAG = "SearchRepositoryImpl"

    override suspend fun search(query: String): TrackResponse {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Starting search for query: $query")
                val response = iTunesService.search(query).execute()
                if (response.isSuccessful) {
                    Log.d(TAG, "Search successful: ${response.body()?.results?.size ?: 0} results found")
                    response.body() ?: throw Exception("Empty response body")
                } else {
                    val errorMessage = "Search failed with error: ${response.code()} - ${response.message()}"
                    Log.e(TAG, errorMessage)
                    throw Exception(errorMessage)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Search error: ${e.message}", e)
                throw e
            }
        }
    }
}
