package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.model.TrackResponseData

interface SearchDataSource {
    suspend fun search(query: String): TrackResponseData
}