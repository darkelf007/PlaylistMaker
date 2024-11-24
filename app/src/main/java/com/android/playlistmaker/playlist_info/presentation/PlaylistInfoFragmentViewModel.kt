package com.android.playlistmaker.playlist_info.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.search.domain.SearchTrack
import kotlinx.coroutines.launch


class PlaylistInfoFragmentViewModel(
    private val currentPlaylistInteractor: CurrentPlaylistInteractor
) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> get() = _playlist

    private val _tracks = MutableLiveData<List<SearchTrack>>()
    val tracks: LiveData<List<SearchTrack>> get() = _tracks

    fun getPlaylistById(playlistId: Long, trackIds: List<Int>) {
        viewModelScope.launch {
            val playlist = currentPlaylistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(playlist)

            val trackList = currentPlaylistInteractor.getTracksByIds(trackIds)
            _tracks.postValue(trackList)
        }
    }

    fun loadPlaylistData(playlistId: Long) {
        viewModelScope.launch {
            val playlist = currentPlaylistInteractor.getPlaylistById(playlistId)
            Log.d("ViewModel", "Loaded playlist: $playlist")
            _playlist.postValue(playlist)

            playlist?.let {
                val trackIds = it.listOfTracksId.split(",").mapNotNull { id -> id.toIntOrNull() }
                val trackList = currentPlaylistInteractor.getTracksByIds(trackIds)
                Log.d("ViewModel", "Loaded tracks: $trackList")
                _tracks.postValue(trackList)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            currentPlaylistInteractor.deletePlaylist(playlist)
        }
    }
}
