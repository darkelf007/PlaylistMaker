package com.android.playlistmaker.di

import com.android.playlistmaker.player.domain.PlayerUseCase
import com.android.playlistmaker.player.data.PlayerUseCaseImpl
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.google.gson.Gson
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::PlayerUseCaseImpl) bind PlayerUseCase::class
    singleOf(::GetThemeUseCase)
    singleOf(::ToggleThemeUseCase)
    singleOf(::ShowUserAgreementUseCase)
    singleOf(::SendSupportEmailUseCase)
    singleOf(::ShowShareDialogUseCase)
    factoryOf(::Gson)
}