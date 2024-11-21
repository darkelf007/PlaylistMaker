package com.android.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseInteractor
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(
    private val playlistMediaDatabaseInteractor: PlaylistMediaDatabaseInteractor
) : ViewModel() {

    private var _databasePlaylistState = MutableLiveData<PlaylistState>()
    var databasePlaylistState: LiveData<PlaylistState> = _databasePlaylistState

    fun fillData() {
        _databasePlaylistState.postValue(PlaylistState.Loading)

        viewModelScope.launch {
            playlistMediaDatabaseInteractor
                .getPlaylistsFromDatabase()
                .collect { listOfPlaylists ->
                    processResult(listOfPlaylists)
                }
        }
    }

    private fun processResult(listOfPlaylists: List<Playlist>) {
        _databasePlaylistState.postValue(
            PlaylistState.Success(listOfPlaylists)
        )
    }
}

