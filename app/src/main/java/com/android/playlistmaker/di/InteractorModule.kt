package com.android.playlistmaker.di


import com.android.playlistmaker.main.domain.MainInteractor
import com.android.playlistmaker.main.domain.MainUseCase
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

}