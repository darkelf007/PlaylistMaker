package com.android.playlistmaker.media.domain.db

import com.android.playlistmaker.media.domain.models.FavoriteTrack
import kotlinx.coroutines.flow.Flow

interface FavoriteDatabaseInteractor {
    suspend fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrack>>
    suspend fun addFavoriteTrack(favoriteTrack: FavoriteTrack)
    suspend fun removeFavoriteTrack(favoriteTrack: FavoriteTrack)
    suspend fun isTrackFavorite(trackId: Int): Boolean
}