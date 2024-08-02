package com.android.playlistmaker.search.presentation.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.search.domain.SearchTrack
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor
) : AndroidViewModel(application) {

    val tracks = MutableLiveData<List<SearchTrack>>()
    val searchHistory = MutableLiveData<List<SearchTrack>>()
    val uiState = MutableLiveData<UiState>()

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
                val response = searchInteractor.search(query)
                if (response.isNotEmpty()) {
                    tracks.value = response
                    updateUiState(UiState.Success)
                } else {
                    updateUiState(UiState.Empty)
                }
            } catch (e: Exception) {
                updateUiState(UiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    private fun updateUiState(state: UiState) {
        if (uiState.value != state) {
            uiState.value = state
        }
    }

    private fun loadSearchHistory() {
        searchHistory.value = searchInteractor.getSearchHistory() ?: emptyList()
    }

    fun addTrackToHistory(searchTrack: SearchTrack) {
        val history = searchHistory.value?.toMutableList() ?: mutableListOf()
        history.removeAll { it.trackId == searchTrack.trackId }
        history.add(0, searchTrack)
        if (history.size > 10) {
            history.removeAt(10)
        }
        searchInteractor.saveSearchHistory(history)
        searchHistory.value = history
    }

    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        searchHistory.value = emptyList()
    }

    fun clearTracks() {
        tracks.value = emptyList()
    }

    fun showHistory() {
        updateUiState(UiState.HistoryVisible)
    }

    fun createTrackJson(searchTrack: SearchTrack): String {
        return Gson().toJson(searchTrack)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getApplication<Application>().applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    sealed class UiState {
        data object Loading : UiState()
        data object Success : UiState()
        data object Empty : UiState()
        data class Error(val message: String) : UiState()
        data object HistoryVisible : UiState()
    }

    override fun onCleared() {
        Log.e("AAA", "VM cleared")
        super.onCleared()
    }
}
