package com.android.playlistmaker.player.domain

interface PlayerUseCase {
    fun prepare(url: String, onPrepared: () -> Unit)
    fun start()
    fun pause()
    fun release()
    fun setOnCompletionListener(onComplete: () -> Unit)
    fun currentPosition(): Int
    fun seekTo(position: Int)
}
