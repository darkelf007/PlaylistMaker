package com.android.playlistmaker.player.presentation

import com.android.playlistmaker.player.domain.Track

data class PlayerViewState(
    val track: Track? = null,
    val playerState: Int = PlayerViewModel.STATE_DEFAULT
)
