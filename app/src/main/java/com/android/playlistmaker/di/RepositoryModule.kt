package com.android.playlistmaker.di


import com.android.playlistmaker.media.data.converter.FavoriteTrackDbConverter
import com.android.playlistmaker.media.data.repository.FavoriteDatabaseRepositoryImpl
import com.android.playlistmaker.media.domain.converter.FavoriteTrackDataConverter
import com.android.playlistmaker.media.domain.converter.FavoriteTrackToTrackConverter
import com.android.playlistmaker.media.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.media.domain.db.FavoriteDatabaseRepository
import com.android.playlistmaker.media.domain.impl.FavoriteDatabaseInteractorImpl
import com.android.playlistmaker.player.data.repository.AudioPlayerDatabaseRepositoryImpl
import com.android.playlistmaker.player.data.repository.PlayerUseCaseImpl
import com.android.playlistmaker.player.domain.interactor.AudioPlayerDatabaseInteractorImpl
import com.android.playlistmaker.player.domain.interfaces.PlayerUseCase
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
    single<FavoriteDatabaseRepository> {
        FavoriteDatabaseRepositoryImpl(
            appDatabase = get(),
            favoriteTrackDbConverter = get()
        )
    }
    single<FavoriteDatabaseInteractor> {
        FavoriteDatabaseInteractorImpl(
            favoriteDatabaseRepository = get(),
            favoriteTrackDataConverter = get(),
            favoriteTrackToTrackConverter = get()
        )
    }
    single { FavoriteTrackDataConverter() }
    single { FavoriteTrackDbConverter() }
    single { FavoriteTrackToTrackConverter() }
    singleOf(::GetThemeUseCase) bind GetThemeUseCaseInterface::class
    singleOf(::ToggleThemeUseCase) bind ToggleThemeUseCaseInterface::class
    singleOf(::ShowUserAgreementUseCase)
    singleOf(::SendSupportEmailUseCase)
    singleOf(::ShowShareDialogUseCase)
    singleOf(::AudioPlayerDatabaseRepositoryImpl)
    singleOf(::AudioPlayerDatabaseInteractorImpl)
}