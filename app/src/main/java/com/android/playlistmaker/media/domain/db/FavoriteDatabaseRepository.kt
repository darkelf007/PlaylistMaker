package com.android.playlistmaker.media.domain.db

import com.android.playlistmaker.media.data.dto.FavoriteTrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteDatabaseRepository {
    suspend fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrackDto>>
    suspend fun addFavoriteTrack(favoriteTrackDto: FavoriteTrackDto)
    suspend fun removeFavoriteTrack(favoriteTrackDto: FavoriteTrackDto)
    suspend fun isTrackFavorite(trackId: Int): Boolean
}