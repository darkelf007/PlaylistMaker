package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.domain.SearchHistoryRepository
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.search.domain.SearchRepository
import com.android.playlistmaker.search.domain.SearchTrack
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchInteractor {

    override fun search(query: String): Flow<List<SearchTrack>> {
        return searchRepository.search(query)
    }

    override fun getSearchHistory(): List<SearchTrack>? {
        return searchHistoryRepository.read()
    }

    override fun saveSearchHistory(history: List<SearchTrack>) {
        searchHistoryRepository.write(history)
    }

    override fun clearSearchHistory() {
        searchHistoryRepository.clearHistory()
    }
}
