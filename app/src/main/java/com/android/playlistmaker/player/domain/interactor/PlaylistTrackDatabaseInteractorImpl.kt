package com.android.playlistmaker.player.domain.interactor

import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseInteractor
import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseRepository
import com.android.playlistmaker.search.domain.SearchTrack


class PlaylistTrackDatabaseInteractorImpl(
    private val playlistTrackDatabaseRepository: PlaylistTrackDatabaseRepository
) : PlaylistTrackDatabaseInteractor {
    override suspend fun insertTrackToPlaylistTrackDatabase(track: SearchTrack) {
        playlistTrackDatabaseRepository.insertTrackToPlaylistTrackDatabase(track)
    }
}