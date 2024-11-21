package com.android.playlistmaker.favorites_tracks.domain.impl


import com.android.playlistmaker.favorites_tracks.domain.converter.FavoriteTrackDataConverter
import com.android.playlistmaker.favorites_tracks.domain.converter.FavoriteTrackToTrackConverter
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseRepository
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoriteDatabaseInteractorImpl(
    private val favoriteDatabaseRepository: FavoriteDatabaseRepository,
    private val favoriteTrackToTrackConverter: FavoriteTrackToTrackConverter,
    private val favoriteTrackDataConverter: FavoriteTrackDataConverter
) : FavoriteDatabaseInteractor {

    override suspend fun getPlayerTracksFromDatabase(): Flow<List<FavoriteTrack>> {
        return favoriteDatabaseRepository.getPlayerTracksFromDatabase()
            .map { list ->
                favoriteTrackToTrackConverter.mapList(list)
            }
    }

    override suspend fun addFavoriteTrack(favoriteTrack: FavoriteTrack) {
        val dto = favoriteTrackDataConverter.map(favoriteTrack)
        favoriteDatabaseRepository.addFavoriteTrack(dto)
    }

    override suspend fun removeFavoriteTrack(favoriteTrack: FavoriteTrack) {
        val dto = favoriteTrackDataConverter.map(favoriteTrack)
        favoriteDatabaseRepository.removeFavoriteTrack(dto)
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return favoriteDatabaseRepository.isTrackFavorite(trackId)
    }

}