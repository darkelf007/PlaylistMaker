package com.android.playlistmaker.media.presentation

import com.android.playlistmaker.new_playlist.domain.models.Playlist


sealed class PlaylistState {
    object Loading : PlaylistState()
    class Success(val data: List<Playlist>) : PlaylistState()
}
