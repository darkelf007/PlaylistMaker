package com.android.playlistmaker.search.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.search.domain.SearchTrack
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val gson: Gson,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _tracks = MutableLiveData<List<SearchTrack>>()
    val tracks: LiveData<List<SearchTrack>> get() = _tracks

    private val _searchHistory = MutableLiveData<List<SearchTrack>>()
    val searchHistory: LiveData<List<SearchTrack>> get() = _searchHistory

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> get() = _uiState

    private val _currentQuery = savedStateHandle.getLiveData<String>("current_query", "")
    val currentQuery: LiveData<String> get() = _currentQuery

    private val queryFlow = MutableStateFlow(_currentQuery.value ?: "")

    private val _navigateToPlayer = MutableLiveData<Event<String>>()
    val navigateToPlayer: LiveData<Event<String>> get() = _navigateToPlayer

    init {
        observeQuery()
    }

    fun updateQuery(query: String) {
        _currentQuery.value = query
        savedStateHandle["current_query"] = query
        queryFlow.value = query
    }

    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .drop(1)
                .distinctUntilChanged()
                .collect { query ->
                    search(query)
                }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            showHistory()
            return
        }

        updateUiState(UiState.Loading)
        viewModelScope.launch {
            searchInteractor.search(query)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    updateUiState(UiState.Error(e.message ?: "Unknown Error"))
                }
                .collect { response ->
                    if (response.isNotEmpty()) {
                        _tracks.value = response
                        updateUiState(UiState.Success)
                    } else {
                        updateUiState(UiState.Empty)
                    }
                }
        }
    }


    fun onTrackClick(searchTrack: SearchTrack) {
        viewModelScope.launch {
            addTrackToHistory(searchTrack)
            _navigateToPlayer.value = Event(createTrackJson(searchTrack))
        }
    }

    private fun updateUiState(state: UiState) {
        _uiState.value = state
    }


    private fun addTrackToHistory(searchTrack: SearchTrack) {
        val history = _searchHistory.value?.toMutableList() ?: mutableListOf()
        history.removeAll { it.trackId == searchTrack.trackId }
        val updatedTrack = searchTrack.copy(insertionTimeStamp = System.currentTimeMillis())
        history.add(0, updatedTrack)
        if (history.size > 10) {
            history.removeAt(10)
        }
        searchInteractor.saveSearchHistory(history)
        _searchHistory.value = history
    }


    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        _searchHistory.value = emptyList()
        updateUiState(UiState.Idle)
    }

    fun clearTracks() {
        _tracks.value = emptyList()
    }

    fun showHistory() {
        viewModelScope.launch {
            val history = searchInteractor.getSearchHistory() ?: emptyList()
            _searchHistory.value = history
            _uiState.value = UiState.History(showHistory = true)
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }

    private fun createTrackJson(searchTrack: SearchTrack): String {
        return gson.toJson(searchTrack)
    }


    sealed class UiState {
        data object Idle : UiState()
        data object Loading : UiState()
        data object Success : UiState()
        data object Empty : UiState()
        data class History(val showHistory: Boolean) : UiState()
        data class Error(val message: String) : UiState()

    }

    class Event<out T>(private val content: T) {
        private var hasBeenHandled = false
        fun getContentIfNotHandled(): T? {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }
    }

}