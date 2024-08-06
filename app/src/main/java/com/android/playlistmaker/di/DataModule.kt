package com.android.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.android.playlistmaker.main.data.MainRepositoryImpl
import com.android.playlistmaker.main.domain.MainRepository
import com.android.playlistmaker.player.data.AudioPlayerImpl
import com.android.playlistmaker.player.domain.AudioRepository
import com.android.playlistmaker.search.data.repository.SearchDataSource
import com.android.playlistmaker.search.data.repository.SearchDataSourceImpl
import com.android.playlistmaker.search.data.repository.SearchHistoryDataSource
import com.android.playlistmaker.search.data.repository.SearchHistoryDataSourceImpl
import com.android.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.android.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.android.playlistmaker.search.domain.SearchHistoryRepository
import com.android.playlistmaker.search.domain.SearchRepository
import com.android.playlistmaker.settings.data.CommunicationRepositoryImpl
import com.android.playlistmaker.settings.data.SettingsRepositoryImpl
import com.android.playlistmaker.settings.domain.SettingsRepository
import com.android.playlistmaker.settings.domain.CommunicationRepositoryInterface
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<MainRepository> { MainRepositoryImpl(get()) }
    single<AudioRepository> { AudioPlayerImpl(get()) }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("HISTORY_PREFERENCES", Context.MODE_PRIVATE)
    }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    single<SearchDataSource> { SearchDataSourceImpl() }
    single<SearchHistoryDataSource> { SearchHistoryDataSourceImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<CommunicationRepositoryInterface> { CommunicationRepositoryImpl(androidContext()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

}



