package com.android.playlistmaker.player.domain

interface AudioRepository {
    fun setDataSource(url: String)
    fun prepareAsync(onPrepared: () -> Unit)
    fun start()
    fun pause()
    fun release()
    fun setOnCompletionListener(onComplete: () -> Unit)
    fun getCurrentPosition(): Int
    fun seekTo(position: Int)
}
