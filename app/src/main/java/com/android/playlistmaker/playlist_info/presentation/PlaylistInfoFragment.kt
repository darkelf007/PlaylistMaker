package com.android.playlistmaker.playlist_info.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentPlaylistInfoBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistInfoFragment : Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null

    private val viewModel: PlaylistInfoFragmentViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistInfoBinding

    private lateinit var backArrow: ImageView
    private lateinit var playlistCover: ImageView
    private lateinit var nameOfPlaylist: TextView
    private lateinit var yearOfPlaylistCreation: TextView
    private lateinit var totalAmountOfMinutes: TextView
    private lateinit var totalNumberOfTracks: TextView
    private lateinit var sharePlaylist: ImageView
    private lateinit var menuOfPlaylist: ImageView

    private lateinit var playlistInfoBottomSheet: LinearLayout
    private lateinit var playlistInfoBottomSheetRecyclerView: RecyclerView

    private lateinit var playlistMenuBottomSheet: LinearLayout
    private lateinit var playlistCoverBottomSheet: ImageView
    private lateinit var nameOfPlaylistBottomSheet: TextView
    private lateinit var totalNumberOfTracksBottomSheet: TextView
    private lateinit var sharePlaylistBottomSheet: FrameLayout
    private lateinit var editPlaylistBottomSheet: FrameLayout
    private lateinit var deletePlaylistBottomSheet: FrameLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw IllegalArgumentException("Activity must implement BottomNavigationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong("playlistId") ?: return
        viewModel.getPlaylistById(playlistId).observe(viewLifecycleOwner, Observer { playlist ->
            playlist?.let { updateUIWithPlaylist(it) }
        })

        backArrow = binding.playlistInfoBack
        playlistCover = binding.playlistInfoCover
        nameOfPlaylist = binding.nameOfPlaylistInfo
        yearOfPlaylistCreation = binding.yearOfPlaylistInfo
        totalAmountOfMinutes = binding.totalMinutesPlaylistInfo
        totalNumberOfTracks = binding.amountOfTracksPlaylistInfo
        sharePlaylist = binding.playlistInfoShare
        menuOfPlaylist = binding.playlistInfoMenu


        playlistInfoBottomSheet = binding.playlistInfoBottomSheet
        playlistInfoBottomSheetRecyclerView = binding.playlistInfoBottomSheetRecyclerview


        playlistMenuBottomSheet = binding.playlistMenuBottomSheet
        playlistCoverBottomSheet = binding.playlistInfoCoverMin
        nameOfPlaylistBottomSheet = binding.nameOfPlaylistInfoMin
        totalNumberOfTracksBottomSheet = binding.amountOfTracksPlaylistInfoMin
        sharePlaylistBottomSheet = binding.sharePlaylist
        editPlaylistBottomSheet = binding.editPlaylist
        deletePlaylistBottomSheet = binding.deletePlaylist


        backArrow.setOnClickListener {
            findNavController().popBackStack(R.id.mediaFragment ,false)

        }



        sharePlaylist.setOnClickListener {

        }

        menuOfPlaylist.setOnClickListener {

        }

        sharePlaylistBottomSheet.setOnClickListener {

        }

        editPlaylistBottomSheet.setOnClickListener {

        }

        deletePlaylistBottomSheet.setOnClickListener {

        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            })

    }

    private fun updateUIWithPlaylist(playlist: Playlist) {
        nameOfPlaylist.text = playlist.name
        yearOfPlaylistCreation.text = playlist.filePath
        totalAmountOfMinutes.text = "${playlist.amountOfTracks} tracks"

    }
}
