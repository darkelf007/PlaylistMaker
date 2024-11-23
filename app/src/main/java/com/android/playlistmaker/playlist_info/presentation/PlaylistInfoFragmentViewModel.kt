package com.android.playlistmaker.playlist_info.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseInteractor
import com.android.playlistmaker.new_playlist.domain.db.PlaylistInteractor
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseInteractor
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository
import kotlinx.coroutines.launch


class PlaylistInfoFragmentViewModel(

    private val playlistDatabaseInteractor: PlaylistInteractor,
    private val playlistTrackDatabaseInteractor: PlaylistTrackDatabaseInteractor,
    private val playlistMediaDatabaseInteractor: PlaylistMediaDatabaseInteractor,
    private val currentPlaylistRepository: CurrentPlaylistRepository,
    private val currentPlaylistInteractor: CurrentPlaylistInteractor,


    ) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> get() = _playlist

    fun getPlaylistById(playlistId: Long): LiveData<Playlist?> {
        viewModelScope.launch {
            val playlist = currentPlaylistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(playlist)
        }
        return _playlist
    }
}