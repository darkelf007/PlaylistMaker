package com.android.playlistmaker.playlist_info.presentation

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
            _playlist.postValue(playlist)

            playlist?.let {
                val trackIds = it.listOfTracksId.split(",").mapNotNull { id -> id.toIntOrNull() }
                val trackList = currentPlaylistInteractor.getTracksByIds(trackIds)
                _tracks.postValue(trackList)
            }
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            currentPlaylistInteractor.deletePlaylist(playlist)
        }
    }

    fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        viewModelScope.launch {
            currentPlaylistInteractor.deleteTrackFromPlaylist(playlistId, trackId)

            val updatedPlaylist = currentPlaylistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(updatedPlaylist)
            val trackIds = updatedPlaylist?.listOfTracksId
                ?.takeIf { it.isNotEmpty() }
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
            if (trackIds.isNullOrEmpty()) {
                _tracks.postValue(emptyList())
            } else {
                val updatedTracks = currentPlaylistInteractor.getTracksByIds(trackIds)
                _tracks.postValue(updatedTracks)
            }
        }
    }
}
