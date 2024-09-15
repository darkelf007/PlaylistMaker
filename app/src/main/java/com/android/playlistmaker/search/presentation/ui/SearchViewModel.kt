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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor,
    private val gson: Gson
) : AndroidViewModel(application) {

    private val _tracks = MutableLiveData<List<SearchTrack>>()
    val tracks: LiveData<List<SearchTrack>> get() = _tracks

    private val _searchHistory = MutableLiveData<List<SearchTrack>>()
    val searchHistory: LiveData<List<SearchTrack>> get() = _searchHistory

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    private val queryFlow = MutableStateFlow("")

    private val _navigateToPlayer = MutableLiveData<Event<String>>()
    val navigateToPlayer: LiveData<Event<String>> get() = _navigateToPlayer

    private var isClickAllowed = true

    init {
        Log.d("SearchViewModel", "VM created")
        loadSearchHistory()
        observeQuery()
    }

    fun updateQuery(query: String) {
        queryFlow.value = query
    }

    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(SEARCH_DEBOUNCE_DELAY_MILLIS)
                .distinctUntilChanged()
                .collect { query ->
                    search(query)
                }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            updateUiState(UiState.HistoryVisible)
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
            if (clickDebounce()) {
                addTrackToHistory(searchTrack)
                _navigateToPlayer.value = Event(createTrackJson(searchTrack))
            }
        }
    }

    private suspend fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            delay(CLICK_DEBOUNCE_DELAY_MILLIS)
            isClickAllowed = true
        }
        return current
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
        return gson.toJson(searchTrack)
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

    override fun onCleared() {
        Log.d("SearchViewModel", "VM cleared")
        super.onCleared()
    }

    private companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
