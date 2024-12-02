package com.android.playlistmaker.playlist_info.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.search.domain.SearchTrack
import kotlinx.coroutines.launch
import java.io.File


class PlaylistInfoFragmentViewModel(
    private val currentPlaylistInteractor: CurrentPlaylistInteractor
) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> get() = _playlist

    private val _tracks = MutableLiveData<List<SearchTrack>>()
    val tracks: LiveData<List<SearchTrack>> get() = _tracks

    private val _coverUri = MutableLiveData<Uri?>()
    val coverUri: LiveData<Uri?> get() = _coverUri

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

            playlist?.filePath?.let { filePath ->
                if (filePath.isBlank()) {
                    _coverUri.postValue(null)
                } else {
                    val path = currentPlaylistInteractor.getPlaylistCoverPath(filePath)
                    val uri = path?.let { Uri.fromFile(File(it)) }
                    _coverUri.postValue(if (uri != null && fileExists(uri)) uri else null)
                }
            } ?: _coverUri.postValue(null)

            val trackList = currentPlaylistInteractor.getTracksByPlaylistId(playlistId)
            _tracks.postValue(trackList)
        }
    }


    private fun fileExists(uri: Uri): Boolean {
        val file = File(uri.path ?: return false)
        return file.exists()
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            currentPlaylistInteractor.deletePlaylist(playlist)
        }
    }

    fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        viewModelScope.launch {
            val isInOtherPlaylists = currentPlaylistInteractor.isTrackInOtherPlaylists(trackId)

            if (!isInOtherPlaylists) {
                currentPlaylistInteractor.deleteTrackCompletely(trackId)
            }

            currentPlaylistInteractor.deleteTrackFromPlaylist(playlistId, trackId)

            val updatedPlaylist = currentPlaylistInteractor.getPlaylistById(playlistId)
            _playlist.postValue(updatedPlaylist)

            val updatedTracks = currentPlaylistInteractor.getTracksByPlaylistId(playlistId)
            _tracks.postValue(updatedTracks)
        }
    }
}
