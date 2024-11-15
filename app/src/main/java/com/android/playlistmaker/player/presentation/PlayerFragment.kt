package com.android.playlistmaker.player.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentPlayerBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.player.domain.models.PlayerTrack
import com.android.playlistmaker.search.domain.SearchTrack
import com.android.playlistmaker.search.domain.mapToPlayerTrack
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private var bottomNavigationListener: BottomNavigationListener? = null

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var addToPlaylistButton: ImageButton
    private lateinit var createPlaylistButton: Button
    private lateinit var playlistRecyclerView: RecyclerView
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var overlay: View

    private var track: SearchTrack? = null

    private var allowToEmit = false

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private var adapter: PlaylistBottomSheetAdapter? = null

    private lateinit var playerTrack: PlayerTrack

    private lateinit var playerViewModel: PlayerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw RuntimeException("$context must implement BottomNavigationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        track = arguments?.getParcelable<SearchTrack>("playerTrack")
            ?: throw IllegalArgumentException("playerTrack аргумент отсутствует")

        track?.let {
            playerTrack = it.mapToPlayerTrack()
        } ?: throw IllegalArgumentException("playerTrack аргумент отсутствует")

        if (playerTrack.previewUrl == null) {
            binding.playerPlayTrack.isEnabled = false
        }
        playerViewModel = getViewModel(parameters = { parametersOf(playerTrack) })

        allowToEmit = false

        playerViewModel.preparePlayer(playerTrack.previewUrl ?: "")
        playerViewModel.assignValToPlayerTrackForRender()

        adapter = PlaylistBottomSheetAdapter(requireContext()) { playlist ->
            clickOnItem(playlist)
        }

        addToPlaylistButton = binding.addToPlaylistButton
        createPlaylistButton = binding.createPlaylistBottomSheetButton
        overlay = binding.overlay
        playlistRecyclerView = binding.playlistsBottomSheetRecyclerview

        playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        playlistRecyclerView.adapter = adapter

        bottomSheetContainer = binding.playlistBottomSheet

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            })

        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }

                    else -> {
                        overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0f) {
                    overlay.alpha = slideOffset
                }
            }
        })

        addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        createPlaylistButton.setOnClickListener {
            playerViewModel.releasePlayer()
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        initialization()

        lifecycleScope.launch {
            playerViewModel.currentPosition.collectLatest { position ->
                binding.previewTrackLength.text = DateTimeUtil.formatTime(position)
            }
        }

        playerViewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            state.track?.let { updateUIWithTrack(it) }
            updatePlaybackState(state)
            updateFavoriteButton(state.isFavorite)
        })

        binding.playerPlayTrack.setOnClickListener {
            playbackControl()
        }
        binding.playerLikeTrack.setOnClickListener {
            playerViewModel.toggleFavorite()
        }

        playerViewModel.playlistsFromDatabase.observe(viewLifecycleOwner) { listOfPlaylists ->
            addPlaylistsToBottomSheetRecyclerView(listOfPlaylists)
        }

        playerViewModel.checkIsTrackInPlaylist.observe(viewLifecycleOwner) { playlistTrackState ->
            if (allowToEmit) {
                if (playlistTrackState.trackIsInPlaylist) {
                    val toastPhrase =
                        getString(R.string.track_already_added) + " ${playlistTrackState.nameOfPlaylist}"
                    Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()
                } else {
                    val toastPhrase =
                        getString(R.string.track_added) + " ${playlistTrackState.nameOfPlaylist}"
                    Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()
                    playerViewModel.getPlaylists()
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

        }

        playerViewModel.allowToCleanTimer = true
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(true)
        playerViewModel.getPlaylists()
        if (playerViewModel.allowToCleanTimer) {
            binding.trackLengthValue.text = "00:00"
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerViewModel.viewState.value?.playerState == PlayerViewModel.STATE_PLAYING) {
            playerViewModel.pausePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        hideBottomNavigation(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun updateFavoriteButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.playerLikeTrack.setImageResource(R.drawable.red_like_button)
        } else {
            binding.playerLikeTrack.setImageResource(R.drawable.like_button)
        }
    }

    private fun updateUIWithTrack(track: PlayerTrack) {
        binding.playerTrackName.text = track.trackName
        binding.playerArtistName.text = track.artistName

        val coverUrl = track.getCoverArtwork()
        if (!coverUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(coverUrl)
                .placeholder(R.drawable.placeholder_album_player)
                .error(R.drawable.placeholder_album_player)
                .centerCrop()
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.dp_8)))
                .into(binding.trackAlbumPlaceholder)
        } else {
            binding.trackAlbumPlaceholder.setImageResource(R.drawable.placeholder_album_player)
        }

        handleUiState(
            if (track.trackTimeMillis != 0) UiState.TrackTimeAvailable(track.trackTimeMillis)
            else UiState.TrackTimeUnavailable
        )
        handleUiState(
            if (!track.collectionName.isNullOrEmpty()) UiState.CollectionNameAvailable(track.collectionName)
            else UiState.CollectionNameUnavailable
        )
        handleUiState(
            if (!track.releaseDate.isNullOrEmpty()) UiState.ReleaseYearAvailable(
                track.releaseDate.split("-")[0]
            ) else UiState.ReleaseYearUnavailable
        )
        handleUiState(
            if (!track.primaryGenreName.isNullOrEmpty()) UiState.GenreAvailable(track.primaryGenreName)
            else UiState.GenreUnavailable
        )
        handleUiState(
            if (!track.country.isNullOrEmpty()) UiState.CountryAvailable(track.country)
            else UiState.CountryUnavailable
        )
    }

    private fun updatePlaybackState(state: PlayerViewState) {
        when (state.playerState) {
            PlayerViewModel.STATE_PREPARED -> {
                binding.playerPlayTrack.isEnabled = true
                binding.playerPlayTrack.setImageResource(R.drawable.play_button)
            }

            PlayerViewModel.STATE_PLAYING -> {
                binding.playerPlayTrack.setImageResource(R.drawable.pause_button)
            }

            PlayerViewModel.STATE_PAUSED, PlayerViewModel.STATE_DEFAULT -> {
                binding.playerPlayTrack.setImageResource(R.drawable.play_button)
            }
        }
    }

    private fun playbackControl() {
        val state = playerViewModel.viewState.value
        when (state?.playerState) {
            PlayerViewModel.STATE_PLAYING -> {
                playerViewModel.pausePlayer()
            }

            PlayerViewModel.STATE_PREPARED, PlayerViewModel.STATE_PAUSED -> {
                playerViewModel.startPlayer()
            }
        }
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
            is UiState.BackButtonClicked -> {
                findNavController().navigateUp()
            }
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

    private fun hideBottomNavigation(isHide: Boolean) {
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(!isHide)
    }

    private fun clickOnItem(playlist: Playlist) {
        allowToEmit = true
        playerViewModel.checkAndAddTrackToPlaylist(playlist, track)
    }

    fun addPlaylistsToBottomSheetRecyclerView(listOfPlaylists: List<Playlist>) {
        adapter?.playlists?.clear()
        adapter?.playlists?.addAll(listOfPlaylists)
        adapter?.notifyDataSetChanged()
    }


    object DateTimeUtil {
        fun formatTime(timeInMillis: Int): String {
            val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            return simpleDateFormat.format(timeInMillis)
        }
    }


}
