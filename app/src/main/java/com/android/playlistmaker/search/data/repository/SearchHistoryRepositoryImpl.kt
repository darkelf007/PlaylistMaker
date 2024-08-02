package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.domain.SearchTrack
import com.android.playlistmaker.search.domain.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val dataSource: SearchHistoryDataSource) : SearchHistoryRepository {
    override fun clearHistory() = dataSource.clearHistory()
    override fun read(): List<SearchTrack>? = dataSource.read()
    override fun write(searchTracks: List<SearchTrack>) = dataSource.write(searchTracks)
}
