package com.android.playlistmaker.player.domain.interactor

import com.android.playlistmaker.player.data.MediaPlayerWrapper
import com.android.playlistmaker.player.domain.model.Track

class PlayerInteractor(val mediaPlayerWrapper: MediaPlayerWrapper) {
    fun prepare(track: Track, onPrepared: () -> Unit) {
        mediaPlayerWrapper.setDataSource(track.previewUrl)
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
