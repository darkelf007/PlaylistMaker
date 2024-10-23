package com.android.playlistmaker.player.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.media.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.media.domain.models.FavoriteTrack
import com.android.playlistmaker.player.domain.interfaces.PlayerUseCase
import com.android.playlistmaker.player.domain.models.PlayerTrack
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerUseCase: PlayerUseCase,
    private val favoriteDatabaseInteractor: FavoriteDatabaseInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val gson: Gson
) : ViewModel() {


    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> get() = _currentPosition

    private val _viewState = MutableLiveData(PlayerViewState())
    val viewState: LiveData<PlayerViewState> get() = _viewState

    private var updateJob: Job? = null

    fun setTrackFromJson(json: String) {
        val track = try {
            gson.fromJson(json, PlayerTrack::class.java)
        } catch (e: Exception) {
            null
        }

        if (track != null) {
            _viewState.value = _viewState.value?.copy(track = track)
            track.previewUrl?.let { preparePlayer(it) }
            checkIfFavorite(track.trackId)
        }
    }

    private fun checkIfFavorite(trackId: Int) {
        viewModelScope.launch {
            val isFav = favoriteDatabaseInteractor.isTrackFavorite(trackId)
            _viewState.value = _viewState.value?.copy(isFavorite = isFav)
        }
    }

    fun toggleFavorite() {
        val track = _viewState.value?.track ?: return
        viewModelScope.launch {
            if (_viewState.value?.isFavorite == true) {
                favoriteDatabaseInteractor.removeFavoriteTrack(
                    FavoriteTrack(
                        trackId = track.trackId,
                        trackName = track.trackName,
                        artistName = track.artistName,
                        trackTimeMillis = track.trackTimeMillis,
                        artworkUrl100 = track.artworkUrl100,
                        collectionName = track.collectionName,
                        releaseDate = track.releaseDate,
                        primaryGenreName = track.primaryGenreName,
                        country = track.country,
                        previewUrl = track.previewUrl
                    )
                )
                _viewState.value = _viewState.value?.copy(isFavorite = false)
            } else {
                favoriteDatabaseInteractor.addFavoriteTrack(
                    FavoriteTrack(
                        trackId = track.trackId,
                        trackName = track.trackName,
                        artistName = track.artistName,
                        trackTimeMillis = track.trackTimeMillis,
                        artworkUrl100 = track.artworkUrl100,
                        collectionName = track.collectionName,
                        releaseDate = track.releaseDate,
                        primaryGenreName = track.primaryGenreName,
                        country = track.country,
                        previewUrl = track.previewUrl
                    )
                )
                _viewState.value = _viewState.value?.copy(isFavorite = true)
            }
        }
    }

    fun preparePlayer(url: String) {
        playerUseCase.prepare(url) {
            _viewState.value = _viewState.value?.copy(playerState = STATE_PREPARED)
            savedStateHandle["playerState"] = STATE_PREPARED
        }
        playerUseCase.setOnCompletionListener {
            updateJob?.cancel()
            playerUseCase.seekTo(0)
            _currentPosition.value = 0
            _viewState.value = _viewState.value?.copy(
                playerState = STATE_PREPARED
            )
            savedStateHandle["playerState"] = STATE_PREPARED
            savedStateHandle["currentPosition"] = 0
        }
    }

    fun startPlayer() {
        if (_viewState.value?.playerState == STATE_PREPARED || _viewState.value?.playerState == STATE_PAUSED) {
            playerUseCase.start()
            _viewState.value = _viewState.value?.copy(playerState = STATE_PLAYING)
            savedStateHandle["playerState"] = STATE_PLAYING
            updateCurrentPosition()
        }
    }

    fun pausePlayer() {
        if (_viewState.value?.playerState == STATE_PLAYING) {
            playerUseCase.pause()
            updateJob?.cancel()
            val position = playerUseCase.currentPosition()
            _currentPosition.value = position
            _viewState.value = _viewState.value?.copy(playerState = STATE_PAUSED)
            savedStateHandle["playerState"] = STATE_PAUSED
            savedStateHandle["currentPosition"] = position
        } else {
            updateJob?.cancel()
        }
    }

    fun releasePlayer() {
        playerUseCase.release()
        _viewState.value = _viewState.value?.copy(playerState = STATE_DEFAULT)
        savedStateHandle["playerState"] = STATE_DEFAULT
        updateJob?.cancel()
    }


    private fun updateCurrentPosition() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (isActive && _viewState.value?.playerState == STATE_PLAYING) {
                val position = playerUseCase.currentPosition()
                _currentPosition.value = position
                delay(DELAY_MILLIS)
            }
        }
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        private const val DELAY_MILLIS = 300L
    }

    override fun onCleared() {
        super.onCleared()
    }
}
