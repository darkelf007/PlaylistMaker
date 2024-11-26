package com.android.playlistmaker.playlist_info.data

import android.util.Log
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistRepository
import com.android.playlistmaker.search.domain.SearchTrack

class CurrentPlaylistInteractorImpl(
    private val currentPlaylistRepository: CurrentPlaylistRepository
) : CurrentPlaylistInteractor {

    override suspend fun getTracksByIds(ids: List<Int>): List<SearchTrack> {
        Log.d("Interactor", "Requesting tracks with IDs: $ids")

        val tracks = currentPlaylistRepository.getTracksByIds(ids)


        Log.d("Interactor", "Received ${tracks.size} track(s)")

        return tracks
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist? {
        Log.d("Interactor", "Requesting playlist with ID: $playlistId")

        val playlist = currentPlaylistRepository.getPlaylistById(playlistId)
        if (playlist == null) {
            Log.w("Interactor", "Playlist with ID=$playlistId not found")
        } else {
            Log.d("Interactor", "Fetched playlist: ID=${playlist.id}, Name=${playlist.name}, TrackTime=${playlist.trackTime}")
        }

        return playlist
    }
    override suspend fun deletePlaylist(playlist: Playlist) {
        currentPlaylistRepository.deletePlaylist(playlist)
    }
    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        currentPlaylistRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }
}