package com.android.playlistmaker.search.domain.repository

import com.android.playlistmaker.search.data.model.TrackResponse

interface SearchRepository {
    suspend fun search(query: String): TrackResponse
}