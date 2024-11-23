package com.android.playlistmaker.playlist_info.data

import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository

class CurrentPlaylistInteractorImpl(
    private val currentPlaylistRepository: CurrentPlaylistRepository
) : CurrentPlaylistInteractor {

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        return currentPlaylistRepository.getPlaylistById(playlistId)
    }
}