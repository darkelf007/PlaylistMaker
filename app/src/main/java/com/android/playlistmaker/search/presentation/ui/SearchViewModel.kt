package com.android.playlistmaker.search.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.search.domain.SearchTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var searchJob: Job? = null

    private val _showHistory = MutableLiveData<Boolean>()
    val showHistory: LiveData<Boolean> get() = _showHistory


    private val _tracks = MutableLiveData<List<SearchTrack>>()
    val tracks: LiveData<List<SearchTrack>> get() = _tracks

    private val _searchHistory = MutableLiveData<List<SearchTrack>>()
    val searchHistory: LiveData<List<SearchTrack>> get() = _searchHistory

    private val _uiState = MutableLiveData<UiState>(UiState.Idle)
    val uiState: LiveData<UiState> get() = _uiState

    private val _currentQuery = savedStateHandle.getLiveData<String>("current_query", "")
    val currentQuery: LiveData<String> get() = _currentQuery

    private val queryFlow = MutableSharedFlow<String>(replay = 0)

    private val _navigateToPlayer = MutableLiveData<Event<SearchTrack>>()
    val navigateToPlayer: LiveData<Event<SearchTrack>> get() = _navigateToPlayer


    init {
        observeQuery()
        loadInitialData()
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
            val history = searchInteractor.getSearchHistory() ?: emptyList()
            _searchHistory.value = history
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val initialQuery = _currentQuery.value ?: ""
            if (initialQuery.isNotEmpty()) {
                search(initialQuery)
            } else {
                updateUiState(UiState.Idle)
            }
        }
    }

    fun evaluateShowHistory(hasFocus: Boolean, query: String) {
        if (query.isEmpty() && hasFocus) {
            _uiState.value =
                UiState.History(showHistory = searchHistory.value?.isNotEmpty() == true)
        } else if (query.isEmpty()) {
            _uiState.value = UiState.Idle
        }
        _showHistory.value =
            hasFocus && query.isEmpty() && (searchHistory.value?.isNotEmpty() == true)
    }

    private fun observeQuery() {
        viewModelScope.launch {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isNotBlank()) {
                        searchInteractor.search(query)
                            .flowOn(Dispatchers.IO)
                            .catch { e ->
                                emit(emptyList())
                            }
                    } else {
                        flowOf(emptyList())
                    }
                }
                .collect { response ->
                    if (response.isNotEmpty()) {
                        _tracks.value = response
                        updateUiState(UiState.Success)
                    } else if (currentQuery.value?.isNotBlank() == true) {
                        updateUiState(UiState.Empty)
                    } else {
                        updateUiState(UiState.Idle)
                    }
                }
        }
    }


    fun search(query: String) {
        if (query.isBlank()) {
            showHistory()
            return
        }

        updateUiState(UiState.Loading)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
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
            val updatedTrack = addTrackToHistory(searchTrack)
            _navigateToPlayer.value = Event(updatedTrack)
        }
    }

    fun updateUiState(state: UiState) {
        if (_uiState.value != state) {
            _uiState.value = state
        }
    }


    private fun addTrackToHistory(searchTrack: SearchTrack): SearchTrack {
        val history = _searchHistory.value?.toMutableList() ?: mutableListOf()
        history.removeAll { it.trackId == searchTrack.trackId }
        val updatedTrack = searchTrack.copy(insertionTimeStamp = System.currentTimeMillis())
        history.add(0, updatedTrack)
        if (history.size > 10) {
            history.removeAt(10)
        }
        searchInteractor.saveSearchHistory(history)
        _searchHistory.value = history
        return updatedTrack
    }


    fun clearSearchHistory() {
        searchInteractor.clearSearchHistory()
        _searchHistory.value = emptyList()
        updateUiState(UiState.Idle)
    }

    fun clearTracks() {
        _tracks.value = emptyList()
    }

    fun updateQuery(query: String, hasFocus: Boolean) {
        _currentQuery.value = query
        savedStateHandle["current_query"] = query

        viewModelScope.launch {
            queryFlow.emit("")
            queryFlow.emit(query)
        }

        evaluateShowHistory(hasFocus, query)
    }

    fun showHistory() {
        viewModelScope.launch {
            val history = searchInteractor.getSearchHistory() ?: emptyList()
            _searchHistory.value = history
            _uiState.value = UiState.History(showHistory = true)
            evaluateShowHistory(true, _currentQuery.value ?: "")
        }
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