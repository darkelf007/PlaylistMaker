package com.android.playlistmaker.search.presentation.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.domain.model.Track
import com.android.playlistmaker.player.presentation.ui.PlayerActivity
import com.android.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.android.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.android.playlistmaker.search.presentation.adapter.TrackAdapter
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val context: Context,
    private val gson: Gson,
    private val searchHistoryRepository: SearchHistoryRepositoryImpl,
    val trackAdapter: TrackAdapter,
    val trackAdapterHistory: TrackAdapter,
    private val playerActivity: Class<PlayerActivity>
) : AndroidViewModel(application) {

    private val TAG = "SearchViewModel"

    val tracks = MutableLiveData<List<Track>>()
    val searchHistory = MutableLiveData<List<Track>>()
    val uiState = MutableLiveData<UiState>()

    private val searchRepository: SearchRepositoryImpl = SearchRepositoryImpl()

    init {
        Log.e("AAA", "VM created")
        loadSearchHistory()
    }

    fun search(query: String) {
        if (query.isBlank()) {
            updateUiState(UiState.HistoryVisible)
            return
        }

        updateUiState(UiState.Loading)
        viewModelScope.launch {
            try {
                val response = searchRepository.search(query)
                if (response.results.isNotEmpty()) {
                    tracks.value = response.results
                    updateUiState(UiState.Success)
                } else {
                    updateUiState(UiState.Empty)
                }
            } catch (e: Exception) {
                updateUiState(UiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun updateUiState(state: UiState) {
        if (uiState.value != state) {
            uiState.value = state
        }
    }

    private fun loadSearchHistory() {
        searchHistory.value = searchHistoryRepository.read() ?: emptyList()
    }

    fun addTrackToHistory(track: Track) {
        val history = searchHistory.value?.toMutableList() ?: mutableListOf()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > 10) {
            history.removeAt(10)
        }
        searchHistoryRepository.write(ArrayList(history))
        searchHistory.value = history
    }

    fun clearSearchHistory() {
        searchHistoryRepository.clearHistory()
        searchHistory.value = emptyList()
    }

    fun clearTracks() {
        tracks.value = emptyList()
    }

    fun showHistory() {
        updateUiState(UiState.HistoryVisible)
    }

    fun createIntentForTrack(track: Track): Intent {
        val playerIntent = Intent(context, playerActivity)
        playerIntent.putExtra(SearchHistoryRepositoryImpl.TRACK, gson.toJson(track))
        return playerIntent
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    sealed class UiState {
        object Loading : UiState()
        object Success : UiState()
        object Empty : UiState()
        data class Error(val message: String) : UiState()
        object HistoryVisible : UiState()
    }

    override fun onCleared() {
        Log.e("AAA", "VM cleared")
        super.onCleared()
    }
}
