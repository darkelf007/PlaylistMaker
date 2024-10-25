package com.android.playlistmaker.player.data.repository

import com.android.playlistmaker.player.domain.interfaces.AudioRepository
import com.android.playlistmaker.player.domain.interfaces.PlayerUseCase

class PlayerUseCaseImpl(
    private val audioRepository: AudioRepository
) : PlayerUseCase {
    override fun prepare(url: String, onPrepared: () -> Unit) {
        audioRepository.setDataSource(url)
        audioRepository.prepareAsync(onPrepared)
    }

    override fun seekTo(position: Int) {
        audioRepository.seekTo(position)
    }

    override fun start() {
        audioRepository.start()
    }

    override fun pause() {
        audioRepository.pause()
    }

    override fun release() {
        audioRepository.release()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        audioRepository.setOnCompletionListener(onComplete)
    }

    override fun currentPosition(): Int {
        return audioRepository.getCurrentPosition()
    }
}
