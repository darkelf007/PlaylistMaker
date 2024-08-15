package com.android.playlistmaker.player.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.player.domain.PlayerUseCase
import com.android.playlistmaker.player.domain.Track
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    init {
        Log.e("PlayerViewModel", "VM created")
    }

    private val _viewState = MutableLiveData(PlayerViewState())
    val viewState: LiveData<PlayerViewState> get() = _viewState

    fun setTrackFromJson(json: String) {
        val track = Gson().fromJson(json, Track::class.java)
        if (track == null) {
            Log.e("PlayerViewModel", "Failed to parse track from JSON")
        } else {
            Log.d("PlayerViewModel", "Track parsed successfully: $track")
            _viewState.value = _viewState.value?.copy(track = track)
            Log.d("PlayerViewModel", "Track preview URL: ${track.previewUrl}")
            preparePlayer(track.previewUrl)
        }
    }

    fun preparePlayer(url: String) {
        Log.d("PlayerViewModel", "Preparing player with URL: $url")
        playerInteractor.prepare(url) {
            Log.d("PlayerViewModel", "Player prepared successfully")
            _viewState.value = _viewState.value?.copy(playerState = STATE_PREPARED)
            savedStateHandle["playerState"] = STATE_PREPARED
        }
        playerInteractor.setOnCompletionListener {
            Log.d("PlayerViewModel", "Track completed")
            _viewState.value = _viewState.value?.copy(
                playerState = STATE_PREPARED,
                currentPosition = 0
            )
            savedStateHandle["playerState"] = STATE_PREPARED
            savedStateHandle["currentPosition"] = 0
            Log.d("PlayerViewModel", "Track completed and reset")
        }
    }

    fun startPlayer() {
        if (_viewState.value?.playerState == STATE_PREPARED || _viewState.value?.playerState == STATE_PAUSED) {
            Log.d("PlayerViewModel", "Starting player")
            playerInteractor.start()
            Log.d("PlayerViewModel", "Player started")
            _viewState.value = _viewState.value?.copy(playerState = STATE_PLAYING)
            savedStateHandle["playerState"] = STATE_PLAYING
            updateCurrentPosition()
        } else {
            Log.e(
                "PlayerViewModel",
                "Cannot start player, incorrect state: ${_viewState.value?.playerState}"
            )
        }
    }

    fun pausePlayer() {
        if (_viewState.value?.playerState == STATE_PLAYING) {
            Log.d("PlayerViewModel", "Pausing player")
            playerInteractor.pause()
            Log.d("PlayerViewModel", "Player paused")
            _viewState.value = _viewState.value?.copy(playerState = STATE_PAUSED)
            savedStateHandle["playerState"] = STATE_PAUSED
        } else {
            Log.e(
                "PlayerViewModel",
                "Cannot pause player, incorrect state: ${_viewState.value?.playerState}"
            )
        }
    }

    fun releasePlayer() {
        Log.d("PlayerViewModel", "Releasing player")
        playerInteractor.release()
        Log.d("PlayerViewModel", "Player released")
        _viewState.value = _viewState.value?.copy(playerState = STATE_DEFAULT)
        savedStateHandle["playerState"] = STATE_DEFAULT
    }

    private fun updateCurrentPosition() {
        viewModelScope.launch {
            while (_viewState.value?.playerState == STATE_PLAYING) {
                val position = playerInteractor.currentPosition()
                Log.d("PlayerViewModel", "Current Position: $position")
                _viewState.value = _viewState.value?.copy(currentPosition = position)
                savedStateHandle["currentPosition"] = position
                delay(DELAY_MILLIS)
                Log.d("PlayerViewModel", "Current Position: $position")
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
        Log.e("PlayerViewModel", "VM cleared")
        super.onCleared()
    }
}
