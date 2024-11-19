package com.android.playlistmaker.di


import com.android.playlistmaker.favorites_tracks.data.converter.FavoriteTrackDbConverter
import com.android.playlistmaker.favorites_tracks.data.repository.FavoriteDatabaseRepositoryImpl
import com.android.playlistmaker.favorites_tracks.domain.converter.FavoriteTrackDataConverter
import com.android.playlistmaker.favorites_tracks.domain.converter.FavoriteTrackToTrackConverter
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseRepository
import com.android.playlistmaker.favorites_tracks.domain.impl.FavoriteDatabaseInteractorImpl
import com.android.playlistmaker.media.data.converter.FavoriteTrackToSearchTrackConverter
import com.android.playlistmaker.media.data.repository.PlaylistMediaDatabaseRepositoryImpl
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseRepository
import com.android.playlistmaker.new_playlist.data.Impl.FileRepositoryImpl
import com.android.playlistmaker.new_playlist.data.Impl.PlaylistRepositoryImpl
import com.android.playlistmaker.new_playlist.domain.db.PlaylistRepository
import com.android.playlistmaker.new_playlist.domain.repository.FileRepository
import com.android.playlistmaker.player.data.repository.AudioPlayerDatabaseRepositoryImpl
import com.android.playlistmaker.player.data.repository.PlayerUseCaseImpl
import com.android.playlistmaker.player.data.repository.PlaylistTrackDatabaseRepositoryImpl
import com.android.playlistmaker.player.domain.interactor.AudioPlayerDatabaseInteractorImpl
import com.android.playlistmaker.player.domain.interfaces.PlayerUseCase
import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseRepository
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.GetThemeUseCaseInterface
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseInterface
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::PlayerUseCaseImpl) bind PlayerUseCase::class

    singleOf(::FavoriteDatabaseRepositoryImpl) bind FavoriteDatabaseRepository::class
    singleOf(::FavoriteDatabaseInteractorImpl) bind FavoriteDatabaseInteractor::class
    single { FavoriteTrackDataConverter() }
    single { FavoriteTrackDbConverter() }
    single { FavoriteTrackToTrackConverter() }
    single { FavoriteTrackToSearchTrackConverter() }


    singleOf(::GetThemeUseCase) bind GetThemeUseCaseInterface::class
    singleOf(::ToggleThemeUseCase) bind ToggleThemeUseCaseInterface::class
    singleOf(::ShowUserAgreementUseCase)
    singleOf(::SendSupportEmailUseCase)
    singleOf(::ShowShareDialogUseCase)
    singleOf(::AudioPlayerDatabaseRepositoryImpl)
    singleOf(::AudioPlayerDatabaseInteractorImpl)
    singleOf(::PlaylistMediaDatabaseRepositoryImpl) bind PlaylistMediaDatabaseRepository::class
    singleOf(::PlaylistTrackDatabaseRepositoryImpl) bind PlaylistTrackDatabaseRepository::class

    singleOf(::FileRepositoryImpl) bind FileRepository::class
    singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class

}