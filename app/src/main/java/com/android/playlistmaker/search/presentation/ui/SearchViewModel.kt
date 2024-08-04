package com.android.playlistmaker.search.presentation.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

    private val _tracks = MutableLiveData<List<SearchTrack>>()
    val tracks: LiveData<List<SearchTrack>> get() = _tracks

    private val _searchHistory = MutableLiveData<List<SearchTrack>>()
    val searchHistory: LiveData<List<SearchTrack>> get() = _searchHistory

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    init {
        Log.d("SearchViewModel", "VM created")
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
                    _tracks.value = response
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
        if (_uiState.value != state) {
            _uiState.value = state
        }
    }

    private fun loadSearchHistory() {
        _searchHistory.value = searchInteractor.getSearchHistory() ?: emptyList()
    }

    fun addTrackToHistory(searchTrack: SearchTrack) {
        val history = _searchHistory.value?.toMutableList() ?: mutableListOf()
        history.removeAll { it.trackId == searchTrack.trackId }
        history.add(0, searchTrack)
        if (history.size > 10) {
            history.removeAt(10)
        }
        searchInteractor.saveSearchHistory(history)
        _searchHistory.value = history
    }

    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        _searchHistory.value = emptyList()
    }

    fun clearTracks() {
        _tracks.value = emptyList()
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
        object Loading : UiState()
        object Success : UiState()
        object Empty : UiState()
        data class Error(val message: String) : UiState()
        object HistoryVisible : UiState()
    }

    override fun onCleared() {
        Log.d("SearchViewModel", "VM cleared")
        super.onCleared()
    }
}
