package com.android.playlistmaker.playlist_info.data

import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository
import com.android.playlistmaker.search.domain.SearchTrack

class CurrentPlaylistInteractorImpl(
    private val currentPlaylistRepository: CurrentPlaylistRepository
) : CurrentPlaylistInteractor {

    override suspend fun getTracksByIds(ids: List<Int>): List<SearchTrack> {
        val tracks = currentPlaylistRepository.getTracksByIds(ids)
        return tracks
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        val playlist = currentPlaylistRepository.getPlaylistById(playlistId)
        return playlist
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        currentPlaylistRepository.deletePlaylist(playlist)
    }

    override suspend fun getTracksByPlaylistId(playlistId: Long): List<SearchTrack> {
        return currentPlaylistRepository.getTracksByPlaylistId(playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        currentPlaylistRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override fun getPlaylistCoverPath(filePath: String): String? {
        return currentPlaylistRepository.getPlaylistCoverPath(filePath)
    }

    override suspend fun isTrackInOtherPlaylists(trackId: Int): Boolean {
        return currentPlaylistRepository.isTrackInOtherPlaylists(trackId)
    }

    override suspend fun deleteTrackCompletely(trackId: Int) {
        currentPlaylistRepository.deleteTrackCompletely(trackId)
    }
}