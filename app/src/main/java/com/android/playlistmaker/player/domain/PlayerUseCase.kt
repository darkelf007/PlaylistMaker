package com.android.playlistmaker.player.domain


class PlayerUseCase(
    private val audioRepository: AudioRepository
) {
    fun prepare(url: String, onPrepared: () -> Unit) {
        audioRepository.setDataSource(url)
        audioRepository.prepareAsync(onPrepared)
    }

    fun start() {
        audioRepository.start()
    }

    fun pause() {
        audioRepository.pause()
    }

    fun release() {
        audioRepository.release()
    }

    fun setOnCompletionListener(onComplete: () -> Unit) {
        audioRepository.setOnCompletionListener(onComplete)
    }

    fun currentPosition(): Int {
        return audioRepository.getCurrentPosition()
    }


}
