package com.android.playlistmaker.player.domain.interfaces

import com.android.playlistmaker.search.domain.SearchTrack


interface PlaylistTrackDatabaseRepository {
    suspend fun insertTrackToPlaylistTrackDatabase(track: SearchTrack, playlistId: Long)
}