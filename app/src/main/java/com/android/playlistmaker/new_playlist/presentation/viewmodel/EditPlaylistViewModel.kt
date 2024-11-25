package com.android.playlistmaker.new_playlist.presentation.viewmodel

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.new_playlist.domain.db.PlaylistInteractor
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.launch
import java.io.File

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = _playlist

    fun loadPlaylistData(playlistId: Long) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(playlist)
        }
    }

    fun updatePlaylist(name: String, description: String, filePath: String?) {
        _playlist.value?.let { currentPlaylist ->
            val updatedPlaylist = currentPlaylist.copy(
                name = name,
                description = description,
                filePath = filePath ?: currentPlaylist.filePath
            )
            Log.d("EditPlaylistViewModel", "Updating playlist: $updatedPlaylist")
            viewModelScope.launch {
                playlistInteractor.updatePlaylist(updatedPlaylist)
                Log.d("EditPlaylistViewModel", "Playlist successfully updated in database")
            }
        } ?: Log.e("EditPlaylistViewModel", "No playlist loaded to update")
    }



}
