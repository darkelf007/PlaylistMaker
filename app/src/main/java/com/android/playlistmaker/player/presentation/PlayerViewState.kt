package com.android.playlistmaker.player.presentation

import com.android.playlistmaker.player.domain.models.PlayerTrack

data class PlayerViewState(
    val track: PlayerTrack? = null,
    val playerState: Int = PlayerViewModel.STATE_DEFAULT,
    val isFavorite: Boolean = false,
    val trackTime: Int? = null,
    val collectionName: String? = null,
    val releaseYear: String? = null,
    val genre: String? = null,
    val country: String? = null
)