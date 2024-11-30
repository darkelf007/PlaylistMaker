package com.android.playlistmaker.playlist_info.domain

import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.search.domain.SearchTrack

interface CurrentPlaylistRepository {
    suspend fun getTracksByIds(ids: List<Int>): List<SearchTrack>
    suspend fun getPlaylistById(playlistId: Long): Playlist?
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int)
    suspend fun getTracksByPlaylistId(playlistId: Long): List<SearchTrack>
}