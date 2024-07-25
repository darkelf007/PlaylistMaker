package com.android.playlistmaker.search.presentation.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.player.presentation.ui.PlayerActivity
import com.android.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.android.playlistmaker.search.presentation.adapter.TrackAdapter
import com.google.gson.Gson


class SearchViewModelFactory(
    private val application: Application,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val gson = Gson()
            val sharedPrefs = application.getSharedPreferences("HISTORY_PREFERENCES", Context.MODE_PRIVATE)
            val searchHistoryRepository = SearchHistoryRepositoryImpl(sharedPrefs)
            val trackAdapter = TrackAdapter(mutableListOf(), context.resources)
            val trackAdapterHistory = TrackAdapter(mutableListOf(), context.resources)
            val playerActivity = PlayerActivity::class.java
            return SearchViewModel(application, context, gson, searchHistoryRepository, trackAdapter, trackAdapterHistory, playerActivity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
