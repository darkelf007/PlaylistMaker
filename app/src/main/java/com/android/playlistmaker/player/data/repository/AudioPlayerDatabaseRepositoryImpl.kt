package com.android.playlistmaker.player.data.repository

import com.android.playlistmaker.db.AppDatabase
import com.android.playlistmaker.player.data.converter.PlayerTrackDbConverter
import com.android.playlistmaker.player.data.dto.PlayerTrackDto
import com.android.playlistmaker.player.domain.interfaces.AudioPlayerDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AudioPlayerDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playerTrackDbConverter: PlayerTrackDbConverter
) : AudioPlayerDatabaseRepository {

    override suspend fun addPlayerTrackToDatabase(playerTrackDto: PlayerTrackDto) {
        appDatabase.trackDao().insertTrack(playerTrackDbConverter.map(playerTrackDto))
    }

    override suspend fun deletePlayerTrackFromDatabase(playerTrackDto: PlayerTrackDto) {
        appDatabase.trackDao().deleteTrack(playerTrackDbConverter.map(playerTrackDto))
    }

    override suspend fun getTracksIdFromDatabase(): Flow<List<Int>> = flow {
        emit(appDatabase.trackDao().getTracksId())
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return appDatabase.trackDao().isTrackFavorite(trackId)
    }
}