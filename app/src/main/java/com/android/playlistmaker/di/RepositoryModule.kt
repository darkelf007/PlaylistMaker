package com.android.playlistmaker.di

import com.android.playlistmaker.player.domain.PlayerUseCase
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import org.koin.dsl.module

val repositoryModule = module {


    single { PlayerUseCase(get()) }
    single { GetThemeUseCase(get()) }
    single { ToggleThemeUseCase(get()) }
    single { ShowUserAgreementUseCase(get()) }
    single { SendSupportEmailUseCase(get()) }
    single { ShowShareDialogUseCase(get()) }
}