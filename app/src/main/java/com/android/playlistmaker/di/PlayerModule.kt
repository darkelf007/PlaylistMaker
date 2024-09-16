package com.android.playlistmaker.di

import android.media.MediaPlayer
import com.android.playlistmaker.player.data.AudioPlayerImpl
import com.android.playlistmaker.player.domain.AudioRepository
import org.koin.dsl.module

val playerModule = module {
    single <MediaPlayer> { MediaPlayer() }
    single<AudioRepository> { AudioPlayerImpl() }
}