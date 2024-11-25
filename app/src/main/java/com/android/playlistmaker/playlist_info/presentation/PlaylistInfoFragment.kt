package com.android.playlistmaker.playlist_info.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
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

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var playlistMenuBottomSheet: LinearLayout


    private lateinit var binding: FragmentPlaylistInfoBinding
    private lateinit var trackAdapter: TrackAdapter

    private var bottomNavigationListener: BottomNavigationListener? = null

    private val args: PlaylistInfoFragmentArgs by navArgs()

    private lateinit var overlay: View

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
        Log.d("PlaylistInfoFragment", "onViewCreated called")
        super.onViewCreated(view, savedInstanceState)

        overlay = binding.overlay
        playlistMenuBottomSheet = binding.playlistMenuBottomSheet

        val infoBottomSheet = view.findViewById<LinearLayout>(R.id.playlist_info_bottom_sheet)

        val infoBottomSheetBehavior = BottomSheetBehavior.from(infoBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }


        menuBottomSheetBehavior = BottomSheetBehavior.from(playlistMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }




        bottomSheetBehavior = BottomSheetBehavior.from(playlistMenuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                overlay.visibility = if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })




        val playlistId = args.playlistId
        Log.d("PlaylistInfoFragment", "Received playlistId: $playlistId")

        setupRecyclerView()

        setupListeners()

        observeViewModel()

        viewModel.loadPlaylistData(playlistId)

    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    override fun onResume() {
        super.onResume()
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(true)
    }

    private fun setupRecyclerView() {
        trackAdapter = TrackAdapter(emptyList(), resources).apply {
            itemClickListener = { track -> navigateToPlayerFragment(track) }
        }

        binding.playlistInfoBottomSheetRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = trackAdapter
        }
    }

    private fun setupListeners() {
        binding.playlistInfoBack.setOnClickListener {
            Log.d("PlaylistInfoFragment", "Back button clicked")
            findNavController().popBackStack()
        }

        binding.playlistInfoShare.setOnClickListener {
            Log.d("PlaylistInfoFragment", "Share button clicked")
        }

        binding.playlistInfoMenu.setOnClickListener {
            Log.d("PlaylistInfoFragment", "Menu button clicked")
            toggleMenuBottomSheet()
        }

        binding.sharePlaylist.setOnClickListener {
            Log.d("PlaylistInfoFragment", "Share Playlist button clicked")
        }

        binding.editPlaylist.setOnClickListener {
            Log.d("PlaylistInfoFragment", "Edit Playlist button clicked")
            val action = PlaylistInfoFragmentDirections
                .actionPlaylistInfoFragmentToEditPlaylistFragment(playlistId = args.playlistId)
            findNavController().navigate(action)
        }

        binding.deletePlaylist.setOnClickListener {
            Log.d("PlaylistInfoFragment", "Delete Playlist button clicked")
            showDeletePlaylistDialog()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d("PlaylistInfoFragment", "Back pressed")
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun observeViewModel() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            Log.d("PlaylistInfoFragment", "Observed playlist: $playlist")
            playlist?.let { updateUIWithPlaylist(it) }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateTracks(tracks)
        }
    }

    private fun updateUIWithPlaylist(playlist: Playlist) {
        Log.d("PlaylistInfoFragment", "Updating UI with playlist: $playlist")
        binding.nameOfPlaylistInfo.text = playlist.name
        binding.yearOfPlaylistInfo.text = playlist.filePath
        binding.totalMinutesPlaylistInfo.text = "${playlist.amountOfTracks} tracks"

        val uri = getValidUri(playlist.filePath)
        Log.d("PlaylistInfoFragment", "Resolved URI: $uri")
        if (uri != null) {
            Log.d("PlaylistInfoFragment", "Setting image URI: $uri")
            binding.playlistInfoCover.setImageURI(uri)
        } else {
            Log.d("PlaylistInfoFragment", "Setting default placeholder image")
            binding.playlistInfoCover.setImageResource(R.drawable.placeholder_playlist_info)
        }
    }

    private fun getValidUri(filePath: String): Uri? {
        val file = if (filePath.startsWith("myalbum/")) {
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), filePath)
        } else {
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum/$filePath")
        }
        Log.d(
            "PlaylistInfoFragment",
            "Checking file existence: ${file.absolutePath}, exists: ${file.exists()}"
        )
        return if (file.exists()) Uri.fromFile(file) else null
    }




    private fun navigateToPlayerFragment(track: SearchTrack) {
        Log.d("PlaylistInfoFragment", "Navigating to PlayerFragment with track: $track")
        val action =
            PlaylistInfoFragmentDirections.actionPlaylistInfoFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun toggleMenuBottomSheet() {
        if (menuBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.delete_playlist_title))
            .setMessage(getString(R.string.delete_playlist_ask))
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                viewModel.deletePlaylist(viewModel.playlist.value!!)
                findNavController().navigateUp()
            }
            .show()
    }

}