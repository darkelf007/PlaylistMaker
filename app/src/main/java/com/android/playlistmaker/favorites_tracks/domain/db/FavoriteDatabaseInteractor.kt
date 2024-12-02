package com.android.playlistmaker.favorites_tracks.domain.db

import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import kotlinx.coroutines.flow.Flow

interface FavoriteDatabaseInteractor {
    fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrack>>
    suspend fun addFavoriteTrack(favoriteTrack: FavoriteTrack)
    suspend fun removeFavoriteTrack(favoriteTrack: FavoriteTrack)
    suspend fun isTrackFavorite(trackId: Int): Boolean
}