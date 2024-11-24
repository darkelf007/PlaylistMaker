package com.android.playlistmaker.playlist_info.domain

import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.search.domain.SearchTrack

interface CurrentPlaylistInteractor {
    suspend fun getTracksByIds(ids: List<Int>): List<SearchTrack>
    suspend fun getPlaylistById(playlistId: Long): Playlist?
}