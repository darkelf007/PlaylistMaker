package com.android.playlistmaker.media.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.databinding.PlaylistFragmentBinding
import com.android.playlistmaker.media.presentation.adapter.PlaylistAdapter
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {


    private val viewModel: PlaylistFragmentViewModel by viewModel()

    private var _binding: PlaylistFragmentBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true

    private lateinit var adapter: PlaylistAdapter

    private lateinit var createPlaylistButton: Button
    private lateinit var emptyPlaylistsPlaceholder: ConstraintLayout
    private lateinit var playlistProgressbar: ProgressBar
    private lateinit var playlistRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        adapter = PlaylistAdapter(requireContext()) { playlist ->
            Log.d("PlaylistFragment", "Clicked on playlist: ${playlist.id}")
            if (clickDebounce()) {
                clickOnItem(playlist)
            }
        }

        createPlaylistButton = binding.newPlaylist
        emptyPlaylistsPlaceholder = binding.emptyPlaylistsPlaceholder
        playlistProgressbar = binding.playlistProgressbar
        playlistRecyclerView = binding.playlistRecyclerView

        playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistRecyclerView.adapter = adapter

        createPlaylistButton.setOnClickListener {
            (parentFragment as? MediaFragment)?.navigateToNewPlaylistFragment()
        }

        viewModel.databasePlaylistState.observe(viewLifecycleOwner) { playlistState ->
            render(playlistState)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun clickOnItem(playlist: Playlist) {
        Log.d("PlaylistFragment", "Navigating to PlaylistInfoFragment with ID: ${playlist.id}")
        val action = MediaFragmentDirections.actionMediaFragmentToPlaylistInfoFragment(playlist.id)
        findNavController().navigate(action)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        Log.d("PlaylistFragment", "clickDebounce called. isClickAllowed: $current")
        if (isClickAllowed) {
            isClickAllowed = false
            Log.d("PlaylistFragment", "isClickAllowed set to false")
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
                Log.d("PlaylistFragment", "isClickAllowed set to true after delay")
            }
        }
        return current
    }

    private fun showLoader() {
        emptyPlaylistsPlaceholder.isVisible = false
        playlistRecyclerView.isVisible = false
        playlistProgressbar.isVisible = true
    }

    private fun showPlaceholder() {
        playlistRecyclerView.isVisible = false
        playlistProgressbar.isVisible = false
        emptyPlaylistsPlaceholder.isVisible = true
    }

    private fun showContent() {
        playlistProgressbar.isVisible = false
        emptyPlaylistsPlaceholder.isVisible = false
        playlistRecyclerView.isVisible = true
    }

    private fun render(playlistState: PlaylistState) {
        when (playlistState) {
            is PlaylistState.Loading -> {
                showLoader()
            }

            is PlaylistState.Success -> {
                if (playlistState.data.isEmpty()) {
                    showPlaceholder()
                } else {
                    playlistState.data.forEach { playlist ->
                        Log.d(
                            "PlaylistFragment",
                            "Rendering playlist: id=${playlist.id}, filePath=${playlist.filePath}"
                        )
                    }
                    adapter.submitList(playlistState.data)
                    showContent()
                }
            }
        }
    }


    companion object {
        fun newInstance() = PlaylistFragment().apply { }
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}