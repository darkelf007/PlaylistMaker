package com.android.playlistmaker.player.domain.interactor

import com.android.playlistmaker.player.domain.converter.PlayerTrackDataConverter
import com.android.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseInteractor
import com.android.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseRepository
import com.android.playlistmaker.player.domain.models.PlayerTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AudioPlayerDatabaseInteractorImpl(
    private val audioPlayerDatabaseRepository: AudioPlayerDatabaseRepository,
    private val playerTrackDataConverter: PlayerTrackDataConverter
) : AudioPlayerDatabaseInteractor {

    override suspend fun addPlayerTrackToDatabase(
        playerTrack: PlayerTrack,
        insertionTimeStamp: Long
    ) {
        audioPlayerDatabaseRepository.addPlayerTrackToDatabase(
            playerTrackDataConverter.map(
                playerTrack,
                insertionTimeStamp
            )
        )
    }

    override suspend fun deletePlayerTrackFromDatabase(playerTrack: PlayerTrack) {
        audioPlayerDatabaseRepository.deletePlayerTrackFromDatabase(
            playerTrackDataConverter.map(
                playerTrack
            )
        )
    }

    override suspend fun getTracksIdFromDatabase(): Flow<List<Int>> {
        return audioPlayerDatabaseRepository.getTracksIdFromDatabase()
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        val favoriteIds =
            audioPlayerDatabaseRepository.getTracksIdFromDatabase().firstOrNull() ?: emptyList()
        return favoriteIds.contains(trackId)
    }
}