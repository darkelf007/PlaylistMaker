package com.android.playlistmaker.di

import com.android.playlistmaker.player.domain.PlayerUseCase
import com.android.playlistmaker.player.domain.PlayerUseCaseImpl
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.google.gson.Gson
import org.koin.dsl.module

val repositoryModule = module {
    single<PlayerUseCase> { PlayerUseCaseImpl(get()) }
    single<GetThemeUseCase> { GetThemeUseCase(get()) }
    single<ToggleThemeUseCase> { ToggleThemeUseCase(get()) }
    single<ShowUserAgreementUseCase> { ShowUserAgreementUseCase(get()) }
    single<SendSupportEmailUseCase> { SendSupportEmailUseCase(get()) }
    single<ShowShareDialogUseCase> { ShowShareDialogUseCase(get()) }
    factory<Gson> { Gson() }
}