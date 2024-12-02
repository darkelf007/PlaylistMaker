package com.android.playlistmaker.new_playlist.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.new_playlist.domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _creationStatus = MutableLiveData<CreationStatus>()
    val creationStatus: LiveData<CreationStatus> = _creationStatus


    fun createPlaylist(name: String, description: String, uriOfImage: Uri?) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(name, description, uriOfImage)
            _creationStatus.value = CreationStatus.Success
        }
    }

    sealed class CreationStatus {
        object Success : CreationStatus()
    }
}
