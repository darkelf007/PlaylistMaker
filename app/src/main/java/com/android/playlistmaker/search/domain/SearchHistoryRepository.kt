package com.android.playlistmaker.search.domain


interface SearchHistoryRepository {
    fun clearHistory()
    fun read(): List<SearchTrack>?
    fun write(searchTracks: List<SearchTrack>)
}
