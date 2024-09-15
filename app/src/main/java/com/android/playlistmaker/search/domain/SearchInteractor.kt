package com.android.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun search(query: String): Flow<List<SearchTrack>>
    fun getSearchHistory(): List<SearchTrack>?
    fun saveSearchHistory(history: List<SearchTrack>)
    fun clearSearchHistory()
}
