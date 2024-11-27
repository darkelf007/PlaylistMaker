package com.android.playlistmaker.favorites_tracks.data.repository

import com.android.playlistmaker.db.AppDatabase
import com.android.playlistmaker.favorites_tracks.data.converter.FavoriteTrackDbConverter
import com.android.playlistmaker.favorites_tracks.data.dto.FavoriteTrackDto
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoriteDatabaseRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteTrackDbConverter: FavoriteTrackDbConverter
) : FavoriteDatabaseRepository {

    override fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrackDto>> {
        return appDatabase.trackDao().getTracksOrderedByInsertionTime()
            .map { dbTrackEntityList ->
                dbTrackEntityList.map { trackEntity -> favoriteTrackDbConverter.map(trackEntity) }
            }
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