package com.android.playlistmaker.playlist_info.data

import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository
import com.android.playlistmaker.search.domain.SearchTrack

class CurrentPlaylistInteractorImpl(
    private val currentPlaylistRepository: CurrentPlaylistRepository
) : CurrentPlaylistInteractor {

    override suspend fun getTracksByIds(ids: List<Int>): List<SearchTrack> {
        return currentPlaylistRepository.getTracksByIds(ids)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        return currentPlaylistRepository.getPlaylistById(playlistId)
    }
    override suspend fun deletePlaylist(playlist: Playlist) {
        currentPlaylistRepository.deletePlaylist(playlist)
    }
}