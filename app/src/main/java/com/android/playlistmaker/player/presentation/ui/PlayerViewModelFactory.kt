package com.android.playlistmaker.player.presentation.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.player.data.MediaPlayerWrapper
import com.android.playlistmaker.player.domain.interactor.PlayerInteractor

class PlayerViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(PlayerViewModel::class.java))
        val mediaPlayerWrapper = MediaPlayerWrapper()
        val playerInteractor = PlayerInteractor(mediaPlayerWrapper)
        return PlayerViewModel(playerInteractor, SavedStateHandle()) as T
    }
}
