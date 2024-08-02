package com.android.playlistmaker.player.data

import android.media.MediaPlayer
import com.android.playlistmaker.player.domain.AudioRepository

class AudioPlayerImpl(private val mediaPlayer: MediaPlayer) : AudioRepository {

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener { onPrepared() }
        mediaPlayer.prepareAsync()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener { onComplete() }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}
