package com.android.playlistmaker.media.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.media.domain.converter.FavoriteTrackToTrackConverter
import com.android.playlistmaker.media.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.media.domain.models.FavoriteTrack
import kotlinx.coroutines.launch

class FavoriteFragmentViewModel(
    private val favoriteDatabaseInteractor: FavoriteDatabaseInteractor,
    private val favoriteTrackToTrackConverter: FavoriteTrackToTrackConverter
) : ViewModel() {
    init {
        Log.d(
            "FavoriteFragmentViewModel",
            "Initialized with interactor: $favoriteDatabaseInteractor and converter: $favoriteTrackToTrackConverter"
        )
    }

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
}
