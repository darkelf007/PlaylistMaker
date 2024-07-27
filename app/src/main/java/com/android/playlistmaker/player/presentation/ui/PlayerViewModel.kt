package com.android.playlistmaker.player.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.domain.model.Track
import com.android.playlistmaker.player.domain.interactor.PlayerInteractor
import com.android.playlistmaker.player.domain.toViewData
import com.android.playlistmaker.player.dto.TrackViewData
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _track = MutableLiveData<TrackViewData>()
    val track: LiveData<TrackViewData> get() = _track

    private val _playerState = MutableLiveData<Int>().apply { value = STATE_DEFAULT }
    val playerState: LiveData<Int> get() = _playerState

    private val _currentPosition = MutableLiveData<Int>().apply { value = 0 }
    val currentPosition: LiveData<Int> get() = _currentPosition

    fun setTrackFromJson(json: String) {
        val track = Gson().fromJson(json, Track::class.java)
        _track.value = track.toViewData()
    }

    fun preparePlayer(url: String) {
        playerInteractor.prepare(url) {
            _playerState.value = STATE_PREPARED
            savedStateHandle["playerState"] = STATE_PREPARED
        }
    }

    fun startPlayer() {
        playerInteractor.start()
        _playerState.value = STATE_PLAYING
        savedStateHandle["playerState"] = STATE_PLAYING
        updateCurrentPosition()
    }

    fun pausePlayer() {
        playerInteractor.pause()
        _playerState.value = STATE_PAUSED
        savedStateHandle["playerState"] = STATE_PAUSED
    }

    fun releasePlayer() {
        playerInteractor.release()
        _playerState.value = STATE_DEFAULT
        savedStateHandle["playerState"] = STATE_DEFAULT
    }

    private fun updateCurrentPosition() {
        viewModelScope.launch {
            while (_playerState.value == STATE_PLAYING) {
                val position = playerInteractor.currentPosition()
                _currentPosition.postValue(position)
                savedStateHandle["currentPosition"] = position
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
        Log.e("AAA", "VM cleared")
        super.onCleared()
    }
}

