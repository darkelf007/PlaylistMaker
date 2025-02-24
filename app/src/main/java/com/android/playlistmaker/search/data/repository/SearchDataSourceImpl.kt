package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.api.iTunesAPI
import com.android.playlistmaker.search.data.model.TrackResponseData


class SearchDataSourceImpl(private val iTunesService: iTunesAPI) : SearchDataSource {

    override suspend fun search(query: String): TrackResponseData {
        println("Query to API: $query")
        val response = iTunesService.search(query)
        println("Response from API: ${response.results}")
        return response
    }
}
