package com.android.playlistmaker.player.presentation


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playlistmaker.favorites_tracks.domain.db.FavoriteDatabaseInteractor
import com.android.playlistmaker.favorites_tracks.domain.models.FavoriteTrack
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseInteractor
import com.android.playlistmaker.new_playlist.domain.db.PlaylistDatabaseInteractor
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.player.domain.interfaces.PlayerUseCase
import com.android.playlistmaker.player.domain.interfaces.PlaylistTrackDatabaseInteractor
import com.android.playlistmaker.player.domain.models.PlayerTrack
import com.android.playlistmaker.search.domain.SearchTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerTrack: PlayerTrack,
    private val playerUseCase: PlayerUseCase,
    private val favoriteDatabaseInteractor: FavoriteDatabaseInteractor,
    private val savedStateHandle: SavedStateHandle,
    private val playlistMediaDatabaseInteractor: PlaylistMediaDatabaseInteractor,
    private val playlistTrackDatabaseInteractor: PlaylistTrackDatabaseInteractor,
    private val playlistDatabaseInteractor: PlaylistDatabaseInteractor
) : ViewModel() {

    private val _viewState = MutableLiveData(PlayerViewState())
    val viewState: LiveData<PlayerViewState> get() = _viewState

    private val _playerTrackForRender = MutableLiveData<PlayerTrack>()


    init {
        assignValToPlayerTrackForRender()
        checkIfFavorite()
    }

    var allowToCleanTimer = true

    private val _checkIsTrackInPlaylist = MutableLiveData<PlaylistTrackState>()
    val checkIsTrackInPlaylist: LiveData<PlaylistTrackState> = _checkIsTrackInPlaylist

    private val _playlistsFromDatabase = MutableLiveData<List<Playlist>>()
    val playlistsFromDatabase: LiveData<List<Playlist>> = _playlistsFromDatabase

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> get() = _currentPosition

    private var updateJob: Job? = null


    fun assignValToPlayerTrackForRender() {
        val playerTrackTo = playerTrack.copy(
            artworkUrl100 = playerTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"),
            releaseDate = playerTrack.releaseDate?.split("-", limit = 2)?.get(0),
            trackTimeMillis = playerTrack.trackTimeMillis
        )

        _playerTrackForRender.postValue(playerTrackTo)
        _viewState.value = _viewState.value?.copy(track = playerTrackTo)
    }

    private fun checkIfFavorite() {
        viewModelScope.launch {
            val isFav = favoriteDatabaseInteractor.isTrackFavorite(playerTrack.trackId)
            _viewState.value = _viewState.value?.copy(isFavorite = isFav)
        }
    }

    fun toggleFavorite() {
        val track = _viewState.value?.track ?: return
        viewModelScope.launch {
            if (_viewState.value?.isFavorite == true) {
                favoriteDatabaseInteractor.removeFavoriteTrack(
                    FavoriteTrack(
                        trackId = track.trackId,
                        trackName = track.trackName,
                        artistName = track.artistName,
                        trackTimeMillis = track.trackTimeMillis,
                        artworkUrl100 = track.artworkUrl100,
                        collectionName = track.collectionName,
                        releaseDate = track.releaseDate,
                        primaryGenreName = track.primaryGenreName,
                        country = track.country,
                        previewUrl = track.previewUrl
                    )
                )
                _viewState.value = _viewState.value?.copy(isFavorite = false)
            } else {
                favoriteDatabaseInteractor.addFavoriteTrack(
                    FavoriteTrack(
                        trackId = track.trackId,
                        trackName = track.trackName,
                        artistName = track.artistName,
                        trackTimeMillis = track.trackTimeMillis,
                        artworkUrl100 = track.artworkUrl100,
                        collectionName = track.collectionName,
                        releaseDate = track.releaseDate,
                        primaryGenreName = track.primaryGenreName,
                        country = track.country,
                        previewUrl = track.previewUrl
                    )
                )
                _viewState.value = _viewState.value?.copy(isFavorite = true)
            }
        }
    }

    fun preparePlayer(url: String) {
        playerUseCase.prepare(url) {
            _viewState.value = _viewState.value?.copy(playerState = STATE_PREPARED)
            savedStateHandle["playerState"] = STATE_PREPARED
        }
        playerUseCase.setOnCompletionListener {
            updateJob?.cancel()
            playerUseCase.seekTo(0)
            _currentPosition.value = 0
            _viewState.value = _viewState.value?.copy(
                playerState = STATE_PREPARED
            )
            savedStateHandle["playerState"] = STATE_PREPARED
            savedStateHandle["currentPosition"] = 0
        }
    }

    fun startPlayer() {
        if (_viewState.value?.playerState == STATE_PREPARED || _viewState.value?.playerState == STATE_PAUSED) {
            playerUseCase.start()
            _viewState.value = _viewState.value?.copy(playerState = STATE_PLAYING)
            savedStateHandle["playerState"] = STATE_PLAYING
            updateCurrentPosition()
            allowToCleanTimer = false
        }
    }

    fun pausePlayer() {
        if (_viewState.value?.playerState == STATE_PLAYING) {
            playerUseCase.pause()
            updateJob?.cancel()
            val position = playerUseCase.currentPosition()
            _currentPosition.value = position
            _viewState.value = _viewState.value?.copy(playerState = STATE_PAUSED)
            savedStateHandle["playerState"] = STATE_PAUSED
            savedStateHandle["currentPosition"] = position
        } else {
            updateJob?.cancel()
        }
    }

    fun releasePlayer() {
        playerUseCase.release()
        _viewState.value = _viewState.value?.copy(playerState = STATE_DEFAULT)
        savedStateHandle["playerState"] = STATE_DEFAULT
        updateJob?.cancel()
    }


    private fun updateCurrentPosition() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            while (isActive && _viewState.value?.playerState == STATE_PLAYING) {
                val position = playerUseCase.currentPosition()
                _currentPosition.value = position
                delay(DELAY_MILLIS)
            }
        }
    }


    fun getPlaylists() {

        viewModelScope.launch {
            playlistMediaDatabaseInteractor.getPlaylistsFromDatabase().collect { listOfPlaylists ->
                _playlistsFromDatabase.postValue(listOfPlaylists)
            }
        }

    }

    private fun insertTrackToDatabase(track: SearchTrack) {

        viewModelScope.launch {
            playlistTrackDatabaseInteractor.insertTrackToPlaylistTrackDatabase(track)
        }

    }

    private fun returnPlaylistToDatabase(playlist: Playlist) {
        viewModelScope.launch {
            playlistDatabaseInteractor.insertPlaylistToDatabase(playlist)
        }
    }

    private fun convertListToString(list: List<Int>): String {
        return if (list.isEmpty()) "" else list.joinToString(separator = ",")
    }

    private fun convertStringToList(string: String): ArrayList<Int> {
        return if (string.isEmpty()) ArrayList() else ArrayList(
            string.split(",").map { it.toInt() })
    }

    fun checkAndAddTrackToPlaylist(playlist: Playlist, track: SearchTrack?) {
        val listIdOfPlaylistTracks: ArrayList<Int> = convertStringToList(playlist.listOfTracksId)

        if (!listIdOfPlaylistTracks.contains(track?.trackId)) {
            track?.let { listIdOfPlaylistTracks.add(it.trackId) }
            val listString = convertListToString(listIdOfPlaylistTracks)
            val modifiedPlaylist: Playlist = playlist.copy(
                listOfTracksId = listString, amountOfTracks = playlist.amountOfTracks + 1
            )
            returnPlaylistToDatabase(modifiedPlaylist)
            track?.let { insertTrackToDatabase(it) }

            _checkIsTrackInPlaylist.postValue(
                PlaylistTrackState(
                    nameOfPlaylist = playlist.name, trackIsInPlaylist = false
                )
            )
        } else {

            _checkIsTrackInPlaylist.postValue(
                PlaylistTrackState(
                    nameOfPlaylist = playlist.name, trackIsInPlaylist = true
                )
            )
        }
    }


    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        private const val DELAY_MILLIS = 300L
    }

}
