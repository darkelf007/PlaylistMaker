package com.android.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import com.android.playlistmaker.player.domain.AudioRepository

class AudioPlayerImpl : AudioRepository {

    private var mediaPlayer: MediaPlayer? = null

    override fun seekTo(position: Int) {
        try {
            mediaPlayer?.seekTo(position)
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to seek media player", e)
        }
    }

    override fun setDataSource(url: String) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            } else {
                mediaPlayer?.reset()
            }
            mediaPlayer?.setDataSource(url)
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to set data source", e)
        } catch (e: Exception) {
            Log.e("AudioPlayerImpl", "Error setting data source", e)
        }
    }

    override fun prepareAsync(onPrepared: () -> Unit) {
        mediaPlayer?.setOnPreparedListener { onPrepared() }
        try {
            mediaPlayer?.prepareAsync()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to prepare asynchronously", e)
        } catch (e: Exception) {
            Log.e("AudioPlayerImpl", "Error preparing asynchronously", e)
        }
    }

    override fun start() {
        try {
            mediaPlayer?.start()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to start media player", e)
        }
    }

    override fun pause() {
        try {
            mediaPlayer?.pause()
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to pause media player", e)
        }
    }

    override fun release() {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to release media player", e)
        }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer?.setOnCompletionListener { onComplete() }
    }

    override fun getCurrentPosition(): Int {
        return try {
            mediaPlayer?.currentPosition ?: 0
        } catch (e: IllegalStateException) {
            Log.e("AudioPlayerImpl", "Failed to get current position", e)
            0
        }
    }
}