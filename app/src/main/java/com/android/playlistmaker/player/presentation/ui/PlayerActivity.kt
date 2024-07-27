package com.android.playlistmaker.player.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityPlayerBinding
import com.android.playlistmaker.player.dto.TrackViewData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var handler: Handler

    private lateinit var playerViewModel: PlayerViewModel

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (playerViewModel.playerState.value == PlayerViewModel.STATE_PLAYING) {
                binding.previewTrackLength.text =
                    DateTimeUtil.formatTime(playerViewModel.currentPosition.value ?: 0)
                handler.postDelayed(this, DELAY_MILLIS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialization()

        handler = Handler(Looper.getMainLooper())

        playerViewModel =
            ViewModelProvider(this, PlayerViewModelFactory()).get(PlayerViewModel::class.java)

        val json = intent.getStringExtra(TRACK)
        playerViewModel.setTrackFromJson(json ?: "")

        playerViewModel.track.observe(this, Observer { trackViewData ->
            trackViewData?.let {
                updateUIWithTrack(it)
                playerViewModel.preparePlayer(it.previewUrl)
            }
        })

        binding.playerPlayTrack.setOnClickListener {
            playbackControl()
        }

        playerViewModel.playerState.observe(this, Observer { state ->
            when (state) {
                PlayerViewModel.STATE_PREPARED -> binding.playerPlayTrack.isEnabled = true
                PlayerViewModel.STATE_PLAYING -> {
                    binding.playerPlayTrack.setImageResource(R.drawable.pause_button)
                    handler.post(timerRunnable)
                }

                PlayerViewModel.STATE_PAUSED, PlayerViewModel.STATE_DEFAULT -> {
                    binding.playerPlayTrack.setImageResource(R.drawable.play_button)
                    handler.removeCallbacks(timerRunnable)
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timerRunnable)
        playerViewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.releasePlayer()
    }

    private fun initialization() {
        binding.backButton.setOnClickListener {
            handleUiState(UiState.BackButtonClicked)
        }
    }

    private fun updateUIWithTrack(trackViewData: TrackViewData) {
        binding.playerTrackName.text = trackViewData.trackName
        binding.playerArtistName.text = trackViewData.artistName
        Glide.with(this).load(trackViewData.coverArtwork)
            .placeholder(R.drawable.placeholder_album_player).centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp_8)))
            .into(binding.trackAlbumPlaceholder)

        handleUiState(if (trackViewData.trackTime != 0) UiState.TrackTimeAvailable(trackViewData.trackTime) else UiState.TrackTimeUnavailable)
        handleUiState(
            if (trackViewData.collectionName.isNotEmpty()) UiState.CollectionNameAvailable(
                trackViewData.collectionName
            ) else UiState.CollectionNameUnavailable
        )
        handleUiState(
            if (trackViewData.releaseDate.isNotEmpty()) UiState.ReleaseYearAvailable(
                trackViewData.releaseDate.split("-")[0]
            ) else UiState.ReleaseYearUnavailable
        )
        handleUiState(
            if (trackViewData.primaryGenreName.isNotEmpty()) UiState.GenreAvailable(
                trackViewData.primaryGenreName
            ) else UiState.GenreUnavailable
        )
        handleUiState(if (trackViewData.country.isNotEmpty()) UiState.CountryAvailable(trackViewData.country) else UiState.CountryUnavailable)
    }

    private fun playbackControl() {
        when (playerViewModel.playerState.value) {
            PlayerViewModel.STATE_PLAYING -> playerViewModel.pausePlayer()
            PlayerViewModel.STATE_PREPARED, PlayerViewModel.STATE_PAUSED -> playerViewModel.startPlayer()
        }
    }

    companion object {
        private const val TRACK = "TRACK"
        private const val DELAY_MILLIS = 300L
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
        object TrackTimeUnavailable : UiState()
        data class CollectionNameAvailable(val name: String) : UiState()
        object CollectionNameUnavailable : UiState()
        data class ReleaseYearAvailable(val year: String) : UiState()
        object ReleaseYearUnavailable : UiState()
        data class GenreAvailable(val genre: String) : UiState()
        object GenreUnavailable : UiState()
        data class CountryAvailable(val country: String) : UiState()
        object CountryUnavailable : UiState()
        object BackButtonClicked : UiState()
    }
}

object DateTimeUtil {
    fun formatTime(timeInMillis: Int): String {
        val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return simpleDateFormat.format(timeInMillis)
    }
}
