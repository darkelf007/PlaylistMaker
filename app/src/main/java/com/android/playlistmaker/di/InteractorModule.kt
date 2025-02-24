package com.android.playlistmaker.di


import com.android.playlistmaker.main.domain.MainInteractor
import com.android.playlistmaker.main.domain.MainUseCase
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseInteractor
import com.android.playlistmaker.media.domain.impl.PlaylistMediaDatabaseInteractorImpl
import com.android.playlistmaker.new_playlist.data.Impl.PlaylistInteractorImpl
import com.android.playlistmaker.new_playlist.domain.db.PlaylistInteractor
import com.android.playlistmaker.player.domain.interactor.PlaylistTrackDatabaseInteractorImpl
import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseInteractor
import com.android.playlistmaker.playlist_info.data.CurrentPlaylistInteractorImpl
import com.android.playlistmaker.playlist_info.domain.CurrentPlaylistInteractor
import com.android.playlistmaker.search.data.repository.SearchInteractorImpl
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.GetThemeUseCaseInterface
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseInterface
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {

    singleOf(::MainUseCase) bind MainInteractor::class
    singleOf(::SearchInteractorImpl) bind SearchInteractor::class
    singleOf(::ToggleThemeUseCase) bind ToggleThemeUseCaseInterface::class
    singleOf(::GetThemeUseCase) bind GetThemeUseCaseInterface::class
    singleOf(::PlaylistMediaDatabaseInteractorImpl) bind PlaylistMediaDatabaseInteractor::class
    singleOf(::PlaylistTrackDatabaseInteractorImpl) bind PlaylistTrackDatabaseInteractor::class

    singleOf(::PlaylistInteractorImpl) bind PlaylistInteractor::class

    singleOf(::CurrentPlaylistInteractorImpl) bind CurrentPlaylistInteractor::class


}