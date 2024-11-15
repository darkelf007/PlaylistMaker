package com.android.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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

    private var adapter: PlaylistAdapter? = null

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
            // Обращаемся к родительскому фрагменту (MediaFragment) для выполнения навигации
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
        Toast.makeText(
            requireContext(), "You clicked on playlist with id ${playlist.id}", Toast.LENGTH_SHORT
        ).show()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }

    private fun View.show(isVisible: Boolean) {
        this.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoader() {
        emptyPlaylistsPlaceholder.show(false)
        playlistRecyclerView.show(false)
        playlistProgressbar.show(true)
    }

    private fun showPlaceholder() {
        playlistRecyclerView.show(false)
        playlistProgressbar.show(false)
        emptyPlaylistsPlaceholder.show(true)
    }

    private fun showContent() {
        playlistProgressbar.show(false)
        emptyPlaylistsPlaceholder.show(false)
        playlistRecyclerView.show(true)
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
                    adapter?.playlists?.clear()
                    adapter?.playlists?.addAll(playlistState.data)
                    adapter?.notifyDataSetChanged()

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