package com.android.playlistmaker.search.domain

class SearchInteractor(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) {

    suspend fun search(query: String): List<SearchTrack> {
        return searchRepository.search(query)
    }

    fun getSearchHistory(): List<SearchTrack>? {
        return searchHistoryRepository.read()
    }

    fun saveSearchHistory(history: List<SearchTrack>) {
        searchHistoryRepository.write(history)
    }

    fun clearSearchHistory() {
        searchHistoryRepository.clearHistory()
    }
}
