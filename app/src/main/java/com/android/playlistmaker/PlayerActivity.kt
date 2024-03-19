package com.android.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var playButton: ImageButton
//    private lateinit var addToPlaylistButton: ImageButton
//    private lateinit var likeButton: ImageButton
    private lateinit var backButton: Button
    private lateinit var trackImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackLength: TextView
    private lateinit var trackLengthGroup: Group
    private lateinit var albumName: TextView
    private lateinit var albumGroup: Group
    private lateinit var releaseYearValue: TextView
    private lateinit var releaseYearGroup: Group
    private lateinit var genreValue: TextView
    private lateinit var genreGroup: Group
    private lateinit var countryName: TextView
    private lateinit var countryGroup: Group
    private lateinit var secsOfListening: TextView
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        initialization()

        val json = intent.getStringExtra("TRACK")
        track = Gson().fromJson(json, Track::class.java)

        updateUIWithTrack(track)
    }

    private fun initialization() {
        playButton = findViewById(R.id.player_play_track)
//        likeButton = findViewById(R.id.player_like_track)
//        addToPlaylistButton = findViewById(R.id.add_to_playlist_button)
        backButton = findViewById(R.id.back_botton)
        trackImage = findViewById(R.id.track_album_placeholder)
        trackName = findViewById(R.id.player_track_name)
        artistName = findViewById(R.id.player_artist_name)
        trackLength = findViewById(R.id.track_lenght_value)
        trackLengthGroup = findViewById(R.id.track_length_group)
        albumName = findViewById(R.id.album_name)
        albumGroup = findViewById(R.id.album_group)
        releaseYearValue = findViewById(R.id.release_year_value)
        releaseYearGroup = findViewById(R.id.release_year_group)
        genreValue = findViewById(R.id.genre_value)
        genreGroup = findViewById(R.id.genre_group)
        countryName = findViewById(R.id.country_name)
        countryGroup = findViewById(R.id.country_group)
        secsOfListening = findViewById(R.id.preview_track_length)

        backButton.setOnClickListener {
            handleUiState(UiState.BackButtonClicked)
        }
    }

    private fun updateUIWithTrack(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        Glide.with(this).load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder_album_player).centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp_8)))
            .into(trackImage)

        handleUiState(if (track.trackTime != 0) UiState.TrackTimeAvailable(track.trackTime) else UiState.TrackTimeUnavailable)
        handleUiState(if (track.collectionName.isNotEmpty()) UiState.CollectionNameAvailable(track.collectionName) else UiState.CollectionNameUnavailable)
        handleUiState(
            if (track.releaseDate.isNotEmpty()) UiState.ReleaseYearAvailable(
                track.releaseDate.split(
                    "-"
                )[0]
            ) else UiState.ReleaseYearUnavailable
        )
        handleUiState(if (track.primaryGenreName.isNotEmpty()) UiState.GenreAvailable(track.primaryGenreName) else UiState.GenreUnavailable)
        handleUiState(if (track.country.isNotEmpty()) UiState.CountryAvailable(track.country) else UiState.CountryUnavailable)
    }

    private fun handleUiState(state: UiState) {
        when (state) {
            is UiState.TrackTimeAvailable -> trackLength.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(state.time)

            is UiState.TrackTimeUnavailable -> trackLengthGroup.visibility = View.GONE
            is UiState.CollectionNameAvailable -> albumName.text = state.name
            is UiState.CollectionNameUnavailable -> albumGroup.visibility = View.GONE
            is UiState.ReleaseYearAvailable -> releaseYearValue.text = state.year
            is UiState.ReleaseYearUnavailable -> releaseYearGroup.visibility = View.GONE
            is UiState.GenreAvailable -> genreValue.text = state.genre
            is UiState.GenreUnavailable -> genreGroup.visibility = View.GONE
            is UiState.CountryAvailable -> countryName.text = state.country
            is UiState.CountryUnavailable -> countryGroup.visibility = View.GONE
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
