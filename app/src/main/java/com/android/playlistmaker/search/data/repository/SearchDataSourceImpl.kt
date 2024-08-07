package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.api.iTunesAPI
import com.android.playlistmaker.search.data.model.TrackResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchDataSourceImpl(private val iTunesService: iTunesAPI) : SearchDataSource {

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
