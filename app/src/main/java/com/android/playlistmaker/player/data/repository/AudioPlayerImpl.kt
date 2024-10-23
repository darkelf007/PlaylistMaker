package com.android.playlistmaker.player.data.repository

import android.media.MediaPlayer
import android.util.Log
import com.android.playlistmaker.player.domain.interfaces.AudioRepository

class AudioPlayerImpl : AudioRepository {

    private var mediaPlayer: MediaPlayer? = null

    override fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun setDataSource(url: String) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
        mediaPlayer?.setDataSource(url)
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer?.setOnPreparedListener { onPrepared() }
        mediaPlayer?.prepareAsync()
    }

    override fun start() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer?.setOnCompletionListener { onComplete() }
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
}