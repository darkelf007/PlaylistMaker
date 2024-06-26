package com.android.playlistmaker.search.domain.repository

import com.android.playlistmaker.domain.model.Track

interface SearchHistoryRepository {
    fun clearHistory()
    fun read(): ArrayList<Track>?
    fun write (tracks: ArrayList<Track>)
}
