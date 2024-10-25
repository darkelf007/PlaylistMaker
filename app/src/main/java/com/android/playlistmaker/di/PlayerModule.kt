package com.android.playlistmaker.di

import android.media.MediaPlayer
import com.android.playlistmaker.player.data.repository.AudioPlayerImpl
import com.android.playlistmaker.player.domain.interfaces.AudioRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val playerModule = module {
    single<MediaPlayer> { MediaPlayer() }
    singleOf(::AudioPlayerImpl) bind AudioRepository::class
}