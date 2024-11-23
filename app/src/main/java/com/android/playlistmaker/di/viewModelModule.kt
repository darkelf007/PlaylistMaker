package com.android.playlistmaker.di

import com.android.playlistmaker.favorites_tracks.presentation.FavoriteFragmentViewModel
import com.android.playlistmaker.main.ui.MainViewModel
import com.android.playlistmaker.media.presentation.PlaylistFragmentViewModel
import com.android.playlistmaker.new_playlist.presentation.viewmodel.EditPlaylistViewModel
import com.android.playlistmaker.new_playlist.presentation.viewmodel.NewPlaylistViewModel
import com.android.playlistmaker.player.domain.models.PlayerTrack
import com.android.playlistmaker.player.presentation.PlayerViewModel
import com.android.playlistmaker.playlist_info.presentation.PlaylistInfoFragmentViewModel
import com.android.playlistmaker.search.presentation.ui.SearchViewModel
import com.android.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::FavoriteFragmentViewModel)
    viewModelOf(::PlaylistFragmentViewModel)
    viewModel { PlaylistInfoFragmentViewModel(get(),get(),get(),get(),get()) }
    viewModel { NewPlaylistViewModel(get()) }
    viewModel { EditPlaylistViewModel(get()) }

    viewModel { (playerTrack: PlayerTrack) ->
        PlayerViewModel(
            playerTrack = playerTrack,
            playerUseCase = get(),
            favoriteDatabaseInteractor = get(),
            savedStateHandle = get(),
            playlistMediaDatabaseInteractor = get(),
            playlistTrackDatabaseInteractor = get(),
            playlistInteractor = get()
        )
    }
}