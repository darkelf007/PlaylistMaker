package com.android.playlistmaker.player.domain.interactor

import com.android.playlistmaker.player.data.MediaPlayerWrapper

class PlayerInteractor(val mediaPlayerWrapper: MediaPlayerWrapper) {
    fun prepare(url: String, onPrepared: () -> Unit) {
        mediaPlayerWrapper.setDataSource(url)
        mediaPlayerWrapper.prepareAsync(onPrepared)
    }

    fun start() {
        mediaPlayerWrapper.start()
    }

    fun pause() {
        mediaPlayerWrapper.pause()
    }

    fun release() {
        mediaPlayerWrapper.release()
    }

    fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayerWrapper.setOnCompletionListener(onComplete)
    }

    fun currentPosition(): Int {
        return mediaPlayerWrapper.getCurrentPosition()
    }
}