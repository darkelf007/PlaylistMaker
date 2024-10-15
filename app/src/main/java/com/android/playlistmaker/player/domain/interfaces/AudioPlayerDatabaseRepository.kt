package com.android.playlistmaker.player.domain.interfaces

import com.android.playlistmaker.player.data.dto.PlayerTrackDto
import kotlinx.coroutines.flow.Flow

interface AudioPlayerDatabaseRepository {
    suspend fun addPlayerTrackToDatabase(playerTrackDto: PlayerTrackDto)
    suspend fun deletePlayerTrackFromDatabase(playerTrackDto: PlayerTrackDto)
    suspend fun getTracksIdFromDatabase(): Flow<List<Int>>
    suspend fun isTrackFavorite(trackId: Int): Boolean
}