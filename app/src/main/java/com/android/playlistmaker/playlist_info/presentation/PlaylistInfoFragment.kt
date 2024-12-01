package com.android.playlistmaker.playlist_info.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.search.domain.SearchTrack
import com.android.playlistmaker.search.presentation.adapter.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistInfoFragment : Fragment() {

    private val viewModel: PlaylistInfoFragmentViewModel by viewModel()
    private val args: PlaylistInfoFragmentArgs by navArgs()
    private var bottomNavigationListener: BottomNavigationListener? = null

    private lateinit var binding: FragmentPlaylistInfoBinding
    private lateinit var trackAdapter: TrackAdapter

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var infoBottomSheetBehavior: BottomSheetBehavior<*>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw IllegalArgumentException("Activity must implement BottomNavigationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomSheets()
        setupRecyclerView()
        setupListeners()
        observeViewModel()

        viewModel.loadPlaylistData(args.playlistId)
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(true)
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    private fun setupBottomSheets() {
        infoBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistInfoBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.playlistMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    //
                }
            })
        }
    }

    private fun setupRecyclerView() {
        trackAdapter = TrackAdapter(emptyList(), resources).apply {
            itemClickListener = { track -> navigateToPlayerFragment(track) }
            itemLongClickListener = { track -> showDeleteTrackDialog(track) }
        }

        binding.playlistInfoBottomSheetRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
        }
    }

    private fun setupListeners() {
        binding.playlistInfoBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.playlistInfoShare.setOnClickListener {
            handleSharePlaylist()
        }

        binding.playlistInfoMenu.setOnClickListener {
            toggleMenuBottomSheet()
        }

        binding.sharePlaylist.setOnClickListener {
            handleSharePlaylist()
        }

        binding.editPlaylist.setOnClickListener {
            val action =
                PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToEditPlaylistFragment(
                    playlistId = args.playlistId
                )
            findNavController().navigate(action)
        }

        binding.deletePlaylist.setOnClickListener {
            showDeletePlaylistDialog()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
    }

    private fun observeViewModel() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                updateUIWithPlaylist(it)
                updateMiniUIWithPlaylist(it)
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateTracks(tracks)
            updateMiniTrackCount(tracks.size)
            updateTotalMinutes(tracks)

            if (tracks.isEmpty()) {
                showAutoDismissDialog()
            }
        }

        viewModel.coverUri.observe(viewLifecycleOwner) { uri ->
            updatePlaylistPicture(uri)
            updateMiniPlaylistPicture(uri)
        }
    }


    private fun updateMiniPlaylistPicture(uri: Uri?) {
        binding.playlistInfoCoverMin.apply {
            if (uri != null && isValidUri(uri)) {
                try {
                    setImageURI(uri)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                } catch (e: Exception) {
                    setImageResource(R.drawable.placeholder_playlist_info)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
            } else {
                setImageResource(R.drawable.placeholder_playlist_info)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
        }
    }

    private fun updatePlaylistPicture(uri: Uri?) {
        binding.playlistInfoCover.apply {
            if (uri != null && isValidUri(uri)) {
                try {
                    setImageURI(uri)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                } catch (e: Exception) {
                    setImageResource(R.drawable.placeholder_playlist_info)
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
            } else {
                setImageResource(R.drawable.placeholder_playlist_info)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
        }
    }


    private fun isValidUri(uri: Uri): Boolean {
        if (uri.path.isNullOrBlank()) {
            return false
        }
        val file = File(uri.path ?: return false)
        val exists = file.exists()
        val canRead = file.canRead()
        return exists && canRead
    }

    private fun updateUIWithPlaylist(playlist: Playlist) {
        binding.nameOfPlaylistInfo.text = playlist.name
        binding.description.text = playlist.description

        val tracksText = resources.getQuantityString(
            R.plurals.tracks_plural, playlist.amountOfTracks, playlist.amountOfTracks
        )
        binding.amountOfTracksPlaylistInfo.text = tracksText
    }

    private fun updateTotalMinutes(tracks: List<SearchTrack>) {
        val totalMinutes = tracks.sumOf { it.trackTimeMillis } / 60000
        val minutesText = resources.getQuantityString(
            R.plurals.minutes_plural, totalMinutes, totalMinutes
        )
        binding.totalMinutesPlaylistInfo.text = minutesText
    }

    private fun navigateToPlayerFragment(track: SearchTrack) {
        val action =
            PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun toggleMenuBottomSheet() {
        menuBottomSheetBehavior.state =
            if (menuBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                BottomSheetBehavior.STATE_COLLAPSED
            } else {
                BottomSheetBehavior.STATE_HIDDEN
            }
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.delete_playlist_title))
            .setMessage(getString(R.string.delete_playlist_ask))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist(viewModel.playlist.value!!)
                findNavController().navigateUp()
            }.show()
    }

    private fun showDeleteTrackDialog(track: SearchTrack) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.delete_track_title))
            .setMessage(getString(R.string.delete_track_message))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteTrackFromPlaylist(args.playlistId, track.trackId)
            }.show()
    }

    private fun handleSharePlaylist() {
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        viewModel.playlist.value?.let { playlist ->
            if (playlist.amountOfTracks == 0) {
                showNoTracksDialog()
            } else {
                sharePlaylistDetails(playlist, viewModel.tracks.value ?: emptyList())
            }
        }
    }

    private fun showNoTracksDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setMessage(getString(R.string.share_empty_playlist))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun sharePlaylistDetails(playlist: Playlist, tracks: List<SearchTrack>) {
        val trackDetails = tracks.mapIndexed { index, track ->
            val minutes = track.trackTimeMillis / 60000
            val seconds = (track.trackTimeMillis % 60000) / 1000
            "${index + 1}. ${track.artistName} - ${track.trackName} ($minutes:${
                seconds.toString().padStart(2, '0')
            })"
        }.joinToString("\n")

        val message = buildString {
            append("${playlist.name}\n")
            append("${playlist.description}\n")
            append(
                resources.getQuantityString(
                    R.plurals.tracks_plural, playlist.amountOfTracks, playlist.amountOfTracks
                )
            )
            append("\n\n")
            append(trackDetails)
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_playlist)))
    }

    private fun updateMiniUIWithPlaylist(playlist: Playlist) {
        binding.nameOfPlaylistInfoMin.text = playlist.name
    }

    private fun updateMiniTrackCount(trackCount: Int) {
        val trackText = resources.getQuantityString(
            R.plurals.tracks_plural, trackCount, trackCount
        )
        binding.amountOfTracksPlaylistInfoMin.text = trackText
    }

    private fun showAutoDismissDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.NotificationTheme)
            .setMessage(getString(R.string.notification_empty_playlist))
            .create()

        dialog.show()

        val messageView = dialog.findViewById<TextView>(android.R.id.message)
        messageView?.apply {
            textSize = 18f
            setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_blue))
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }, 3000)
    }
}
