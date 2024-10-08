package com.android.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.android.playlistmaker.search.data.api.iTunesAPI
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
import com.android.playlistmaker.settings.domain.CommunicationRepositoryInterface
import com.android.playlistmaker.settings.domain.SettingsRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<Retrofit> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    single<iTunesAPI> {
        get<Retrofit>().create(iTunesAPI::class.java)
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("search_history_prefs", Context.MODE_PRIVATE)
    }

    singleOf(::Gson)

    singleOf(::SearchDataSourceImpl) bind SearchDataSource::class
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::SearchHistoryDataSourceImpl) bind SearchHistoryDataSource::class
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
    singleOf(::CommunicationRepositoryImpl) bind CommunicationRepositoryInterface::class
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
}



