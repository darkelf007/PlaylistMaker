package com.android.playlistmaker.favorites_tracks.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.favorites_tracks.domain.converter.FavoriteTrackToTrackConverter
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import com.android.playlistmaker.search.domain.SearchTrack

import kotlinx.coroutines.launch

class FavoriteFragmentViewModel(
    private val favoriteDatabaseInteractor: FavoriteDatabaseInteractor,
    private val favoriteTrackToTrackConverter: FavoriteTrackToTrackConverter
) : ViewModel() {

    private val _databaseTracksState = MutableLiveData<FavouriteTrackState>()
    val databaseTracksState: LiveData<FavouriteTrackState> = _databaseTracksState


    fun fillData() {

        _databaseTracksState.postValue(
            FavouriteTrackState(
                libraryTracks = emptyList(),
                isLoading = true
            )
        )

        viewModelScope.launch {
            favoriteDatabaseInteractor
                .getPlayerTracksFromDatabase()
                .collect { libraryTracks ->
                    processResult(libraryTracks)
                }
        }

    }

    private fun processResult(libraryTracks: List<FavoriteTrack>) {
        _databaseTracksState.postValue(
            FavouriteTrackState(
                libraryTracks = libraryTracks,
                isLoading = false
            )
        )
    }

    fun isTrackFavorite(trackId: Int, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavorite = favoriteDatabaseInteractor.isTrackFavorite(trackId)
            callback(isFavorite)
        }
    }

    fun convertFavoriteTrackToTrack(favoriteTrack: FavoriteTrack): SearchTrack {
        return favoriteTrackToTrackConverter.map(favoriteTrack)
    }

}
