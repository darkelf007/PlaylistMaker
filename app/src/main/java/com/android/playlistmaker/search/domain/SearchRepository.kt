package com.android.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun search(query: String): Flow<List<SearchTrack>>
}
