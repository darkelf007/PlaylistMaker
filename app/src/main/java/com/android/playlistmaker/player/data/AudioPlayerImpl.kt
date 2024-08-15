package com.android.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.android.playlistmaker.player.domain.AudioRepository

class AudioPlayerImpl(private val mediaPlayer: MediaPlayer) : AudioRepository {

    override fun setDataSource(url: String) {
        try {
            mediaPlayer.reset()  // Сбрасываем предыдущие состояния
            mediaPlayer.setDataSource(url)
        } catch (e: IllegalStateException) {
            // Логирование ошибки
            Log.e("AudioPlayerImpl", "Failed to set data source", e)
        }
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener { onPrepared() }
        try {
            mediaPlayer.prepareAsync()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to prepare asynchronously", e)
        }
    }

    override fun start() {
        try {
            mediaPlayer.start()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to start media player", e)
        }
    }

    override fun pause() {
        try {
            mediaPlayer.pause()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to pause media player", e)
        }
    }

    override fun release() {
        try {
            mediaPlayer.release()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to release media player", e)
        }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener { onComplete() }
    }

    override fun getCurrentPosition(): Int {
        return try {
            mediaPlayer.currentPosition
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to get current position", e)
            0
        }
    }
}
