package com.android.playlistmaker.player.data

import android.media.MediaPlayer

class MediaPlayerWrapper {
    val mediaPlayer = MediaPlayer()

    fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener { onPrepared() }
        mediaPlayer.prepareAsync()
    }

    fun start() {
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun release() {
        mediaPlayer.release()
    }

    fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener { onComplete() }
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}
