package com.android.playlistmaker.player.domain

import com.android.playlistmaker.domain.model.Track
import com.android.playlistmaker.player.dto.TrackViewData

fun Track.toViewData(): TrackViewData {
    return TrackViewData(
        trackName = this.trackName,
        artistName = this.artistName,
        coverArtwork = this.getCoverArtwork(),
        trackTime = this.trackTime,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )
}
