package com.android.playlistmaker.media.data.repository

import com.android.playlistmaker.db.AppDatabase
import com.android.playlistmaker.db.entity.DBTrackEntity
import com.android.playlistmaker.media.data.converter.FavoriteTrackDbConverter
import com.android.playlistmaker.media.data.dto.FavoriteTrackDto
import com.android.playlistmaker.media.domain.db.FavoriteDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoriteDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteTrackDbConverter: FavoriteTrackDbConverter
) : FavoriteDatabaseRepository {

    override suspend fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrackDto>> =
        getTracksOrderedByInsertionTime()

    suspend fun getTracksOrderedByInsertionTime(): Flow<List<FavoriteTrackDto>> = flow {
        val dbTrackEntityList: List<DBTrackEntity> =
            appDatabase.trackDao().getTracksOrderedByInsertionTime()
        emit(dbTrackEntityList.map { trackEntity -> favoriteTrackDbConverter.map(trackEntity) })
    }

    override suspend fun addFavoriteTrack(favoriteTrackDto: FavoriteTrackDto) {
        appDatabase.trackDao().insertTrack(favoriteTrackDbConverter.map(favoriteTrackDto))
    }

    override suspend fun removeFavoriteTrack(favoriteTrackDto: FavoriteTrackDto) {
        appDatabase.trackDao().deleteTrack(favoriteTrackDbConverter.map(favoriteTrackDto))
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return appDatabase.trackDao().isTrackFavorite(trackId)
    }
}