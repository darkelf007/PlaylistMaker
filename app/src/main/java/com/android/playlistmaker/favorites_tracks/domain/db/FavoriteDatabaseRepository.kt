package com.android.playlistmaker.favorites_tracks.domain.db

import com.android.playlistmaker.favorites_tracks.data.dto.FavoriteTrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteDatabaseRepository {
    fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrackDto>>
    suspend fun addFavoriteTrack(favoriteTrackDto: FavoriteTrackDto)
    suspend fun removeFavoriteTrack(favoriteTrackDto: FavoriteTrackDto)
    suspend fun isTrackFavorite(trackId: Int): Boolean
}