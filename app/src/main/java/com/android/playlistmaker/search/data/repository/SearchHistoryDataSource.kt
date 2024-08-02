package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.domain.SearchTrack

interface SearchHistoryDataSource {
    fun clearHistory()
    fun read(): List<SearchTrack>?
    fun write(searchTracks: List<SearchTrack>)
}

