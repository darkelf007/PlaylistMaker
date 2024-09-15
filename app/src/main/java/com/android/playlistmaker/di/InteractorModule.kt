package com.android.playlistmaker.di


import com.android.playlistmaker.main.domain.MainInteractor
import com.android.playlistmaker.main.domain.MainUseCase
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.search.domain.SearchInteractorImpl
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.GetThemeUseCaseInterface
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseInterface
import org.koin.dsl.module

val interactorModule = module {

    single<MainInteractor> { MainUseCase(get()) }
    single<SearchInteractor> { SearchInteractorImpl(get(), get()) }
    single<ToggleThemeUseCaseInterface> { ToggleThemeUseCase(get()) }
    single<GetThemeUseCaseInterface> { GetThemeUseCase(get()) }

}