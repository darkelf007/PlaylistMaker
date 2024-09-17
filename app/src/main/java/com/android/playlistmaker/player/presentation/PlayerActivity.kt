package com.android.playlistmaker.player.presentation

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityPlayerBinding
import com.android.playlistmaker.player.domain.Track
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private var previousTrack: Track? = null

    private lateinit var binding: ActivityPlayerBinding

    private val playerViewModel: PlayerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialization()
        lifecycleScope.launch {
            playerViewModel.currentPosition.collectLatest { position ->
                binding.previewTrackLength.text = DateTimeUtil.formatTime(position)
            }
        }

        val json = intent.getStringExtra(TRACK)
        Log.d("PlayerActivity", "Received JSON: $json")

        playerViewModel.setTrackFromJson(json ?: "")

        playerViewModel.viewState.observe(this) { state ->
            Log.d("PlayerActivity", "ViewState observed: $state")
            if (state.track != previousTrack) {
                state.track?.let { updateUIWithTrack(it) }
                previousTrack = state.track
            }
            updatePlaybackState(state)
        }

        binding.playerPlayTrack.setOnClickListener {
            Log.d("PlayerActivity", "Play button clicked")
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerViewModel.viewState.value?.playerState == PlayerViewModel.STATE_PLAYING) {
            playerViewModel.pausePlayer()
        }
        Log.d("PlayerActivity", "Activity paused")
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.releasePlayer()
        Log.d("PlayerActivity", "Activity destroyed")
    }

    private fun initialization() {
        binding.backButton.setOnClickListener {
            handleUiState(UiState.BackButtonClicked)
        }
    }

    private fun updateUIWithTrack(track: Track) {
        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName

        val coverUrl = track.getCoverArtwork()
        if (coverUrl.isNullOrEmpty()) {
            Log.e("PlayerActivity", "Cover artwork URL is null or empty")
        } else {
            Log.d("PlayerActivity", "Loading cover artwork from: $coverUrl")
            Glide.with(this).load(coverUrl).placeholder(R.drawable.placeholder_album_player)
                .error(R.drawable.placeholder_album_player).centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp_8)))
                .into(binding.trackAlbumPlaceholder)
        }

        handleUiState(if (track.trackTime != 0) UiState.TrackTimeAvailable(track.trackTime) else UiState.TrackTimeUnavailable)
        handleUiState(
            if (!track.collectionName.isNullOrEmpty()) UiState.CollectionNameAvailable(track.collectionName) else UiState.CollectionNameUnavailable
        )
        handleUiState(
            if (!track.releaseDate.isNullOrEmpty()) UiState.ReleaseYearAvailable(
                track.releaseDate.split("-")[0]
            ) else UiState.ReleaseYearUnavailable
        )
        handleUiState(
            if (!track.primaryGenreName.isNullOrEmpty()) UiState.GenreAvailable(track.primaryGenreName) else UiState.GenreUnavailable
        )
        handleUiState(if (!track.country.isNullOrEmpty()) UiState.CountryAvailable(track.country) else UiState.CountryUnavailable)
    }

    private fun updatePlaybackState(state: PlayerViewState) {
        Log.d("PlayerActivity", "Updating playback state: ${state.playerState}")
        when (state.playerState) {
            PlayerViewModel.STATE_PREPARED -> {
                binding.playerPlayTrack.isEnabled = true
                binding.playerPlayTrack.setImageResource(R.drawable.play_button)
                Log.d("PlayerActivity", "Player state: PREPARED")
            }

            PlayerViewModel.STATE_PLAYING -> {
                binding.playerPlayTrack.setImageResource(R.drawable.pause_button)
                Log.d("PlayerActivity", "Player state: PLAYING")
            }

            PlayerViewModel.STATE_PAUSED, PlayerViewModel.STATE_DEFAULT -> {
                binding.playerPlayTrack.setImageResource(R.drawable.play_button)
                Log.d("PlayerActivity", "Player state: PAUSED or DEFAULT")
            }
        }

        if (state.playerState == PlayerViewModel.STATE_PREPARED || state.playerState == PlayerViewModel.STATE_PLAYING || state.playerState == PlayerViewModel.STATE_PAUSED) {
//            binding.previewTrackLength.text = DateTimeUtil.formatTime(state.currentPosition)
        }
    }

    private fun playbackControl() {
        val state = playerViewModel.viewState.value
        Log.d("PlayerActivity", "Playback control: current state = ${state?.playerState}")
        when (state?.playerState) {
            PlayerViewModel.STATE_PLAYING -> {
                playerViewModel.pausePlayer()
                Log.d("PlayerActivity", "Playback control: PAUSE")
            }

            PlayerViewModel.STATE_PREPARED, PlayerViewModel.STATE_PAUSED -> {
                playerViewModel.startPlayer()
                Log.d("PlayerActivity", "Playback control: PLAY")
            }
        }
    }

    companion object {
        private const val TRACK = "TRACK"
    }

    private fun handleUiState(state: UiState) {
        when (state) {
            is UiState.TrackTimeAvailable -> binding.trackLengthValue.text =
                DateTimeUtil.formatTime(state.time)

            is UiState.TrackTimeUnavailable -> binding.trackLengthGroup.visibility = View.GONE
            is UiState.CollectionNameAvailable -> binding.albumName.text = state.name
            is UiState.CollectionNameUnavailable -> binding.albumGroup.visibility = View.GONE
            is UiState.ReleaseYearAvailable -> binding.releaseYearValue.text = state.year
            is UiState.ReleaseYearUnavailable -> binding.releaseYearGroup.visibility = View.GONE
            is UiState.GenreAvailable -> binding.genreValue.text = state.genre
            is UiState.GenreUnavailable -> binding.genreGroup.visibility = View.GONE
            is UiState.CountryAvailable -> binding.countryName.text = state.country
            is UiState.CountryUnavailable -> binding.countryGroup.visibility = View.GONE
            is UiState.BackButtonClicked -> this.finish()
        }
    }

    sealed class UiState {
        data class TrackTimeAvailable(val time: Int) : UiState()
        data object TrackTimeUnavailable : UiState()
        data class CollectionNameAvailable(val name: String) : UiState()
        data object CollectionNameUnavailable : UiState()
        data class ReleaseYearAvailable(val year: String) : UiState()
        data object ReleaseYearUnavailable : UiState()
        data class GenreAvailable(val genre: String) : UiState()
        data object GenreUnavailable : UiState()
        data class CountryAvailable(val country: String) : UiState()
        data object CountryUnavailable : UiState()
        data object BackButtonClicked : UiState()
    }
}

object DateTimeUtil {
    fun formatTime(timeInMillis: Int): String {
        val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return simpleDateFormat.format(timeInMillis)
    }
}
