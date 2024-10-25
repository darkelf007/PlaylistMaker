package com.android.playlistmaker.player.domain.interfaces

import com.android.playlistmaker.player.domain.models.PlayerTrack
import kotlinx.coroutines.flow.Flow

interface AudioPlayerDatabaseInteractor {
    suspend fun addPlayerTrackToDatabase(playerTrack: PlayerTrack, insertionTimeStamp: Long)
    suspend fun deletePlayerTrackFromDatabase(playerTrack: PlayerTrack)
    suspend fun getTracksIdFromDatabase(): Flow<List<Int>>
    suspend fun isTrackFavorite(trackId: Int): Boolean
}