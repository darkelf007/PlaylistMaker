package com.android.playlistmaker.player.presentation

import android.media.MediaPlayer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.player.data.AudioPlayerImpl
import com.android.playlistmaker.player.domain.PlayerUseCase

class PlayerViewModelFactory(
    private val mediaPlayer: MediaPlayer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(PlayerViewModel::class.java))
        val audioPlayer = AudioPlayerImpl(mediaPlayer)
        val playerUseCase = PlayerUseCase(audioPlayer)
        return PlayerViewModel(playerUseCase, SavedStateHandle()) as T
    }
}
