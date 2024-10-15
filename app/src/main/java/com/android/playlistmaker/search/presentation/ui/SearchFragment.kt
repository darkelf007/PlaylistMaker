package com.android.playlistmaker.search.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.playlistmaker.R
import com.android.playlistmaker.app.App
import com.android.playlistmaker.databinding.FragmentSearchBinding
import com.android.playlistmaker.player.presentation.PlayerActivity
import com.android.playlistmaker.search.presentation.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModel { parametersOf(requireActivity().application) }
    private val TAG = "SearchFragment"
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("FragmentTransition", "SearchFragment created")
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FragmentTransition", "SearchFragment view created")
        Log.d(TAG, "Fragment created")

        trackAdapter = TrackAdapter(mutableListOf(), resources)
        trackAdapterHistory = TrackAdapter(mutableListOf(), resources)

        binding.trackList.adapter = trackAdapter
        binding.trackList.layoutManager = LinearLayoutManager(context)

        binding.searchHistoryRecyclerView.adapter = trackAdapterHistory
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(context)

        setupObservers()
        setupListeners()

        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_ITEM, "")
            binding.inputEditText.setText(searchText)
            if (!searchText.isNullOrEmpty()) {
                searchViewModel.search(searchText)
            }
        }
    }

    private fun setupObservers() {
        Log.d(TAG, "setupObservers called")

        searchViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            Log.d(TAG, "Tracks updated: ${tracks.size} items")
            trackAdapter.updateTracks(tracks)
        })

        searchViewModel.searchHistory.observe(viewLifecycleOwner, Observer { tracksHistory ->
            Log.d(TAG, "Search history updated: ${tracksHistory.size} items")
            trackAdapterHistory.updateTracks(tracksHistory)
            binding.searchHistory.isVisible = tracksHistory.isNotEmpty()

            if (tracksHistory.isNotEmpty()) {
                binding.trackList.isVisible = false
                binding.placeholderImage.isVisible = false
                binding.placeholderText.isVisible = false
                binding.buttonUpdate.isVisible = false

                binding.searchHistoryRecyclerView.isVisible = true
                binding.clearSearchHistory.isVisible = true
            } else {
                binding.trackList.isVisible = true

                binding.searchHistoryRecyclerView.isVisible = false
                binding.clearSearchHistory.isVisible = false
            }
        })

        searchViewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
            Log.d(TAG, "UI State changed: $state")
            handleUiState(state)
        })

        searchViewModel.navigateToPlayer.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { trackJson ->
                val playerIntent = Intent(requireContext(), PlayerActivity::class.java).apply {
                    putExtra(App.KEY_FOR_PLAYER, trackJson)
                }
                startActivity(playerIntent)
            }
        })
    }


    private fun setupListeners() {
        binding.clearIcon.setOnClickListener {
            binding.placeholderImage.isVisible = false
            binding.placeholderText.isVisible = false
            binding.buttonUpdate.isVisible = false
            binding.inputEditText.setText("")
            searchViewModel.hideKeyboard(requireActivity().currentFocus ?: View(requireContext()))
            searchViewModel.clearTracks()
            searchViewModel.showHistory()
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) searchViewModel.showHistory()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.updateQuery(binding.inputEditText.text.toString())
                true
            } else {
                false
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = clearButtonVisibility(s)
                searchViewModel.updateQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.clearSearchHistory.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }

        trackAdapterHistory.itemClickListener = { track ->
            searchViewModel.onTrackClick(track)
        }

        trackAdapter.itemClickListener = { track ->
            searchViewModel.onTrackClick(track)
        }
    }

    private fun handleUiState(state: SearchViewModel.UiState) {
        try {
            when (state) {
                is SearchViewModel.UiState.Loading -> {
                    Log.d(TAG, "handleUiState: Loading")
                    binding.downloadIconFrame.isVisible = true
                    binding.trackList.isVisible = false
                    binding.placeholderImage.isVisible = false
                    binding.placeholderText.isVisible = false
                    binding.buttonUpdate.isVisible = false
                    binding.searchHistory.isVisible = false
                    binding.clearSearchHistory.isVisible = false
                }

                is SearchViewModel.UiState.Success -> {
                    Log.d(TAG, "handleUiState: Success")
                    binding.downloadIconFrame.isVisible = false
                    binding.trackList.isVisible = true
                    binding.placeholderImage.isVisible = false
                    binding.placeholderText.isVisible = false
                    binding.buttonUpdate.isVisible = false
                    binding.searchHistory.isVisible = false
                    binding.clearSearchHistory.isVisible = false
                }

                is SearchViewModel.UiState.Empty -> {
                    Log.d(TAG, "handleUiState: Empty")
                    binding.downloadIconFrame.isVisible = false
                    binding.trackList.isVisible = false
                    binding.placeholderImage.isVisible = true
                    binding.placeholderText.isVisible = true
                    binding.placeholderImage.setImageResource(R.drawable.not_found)
                    binding.placeholderText.text = getString(R.string.not_found)
                    binding.buttonUpdate.isVisible = false
                    binding.searchHistory.isVisible = false
                    binding.clearSearchHistory.isVisible = false
                }

                is SearchViewModel.UiState.Error -> {
                    Log.d(TAG, "handleUiState: Error with message: ${state.message}")
                    binding.downloadIconFrame.isVisible = false
                    binding.trackList.isVisible = false
                    binding.placeholderImage.isVisible = true
                    binding.placeholderText.isVisible = true
                    binding.placeholderImage.setImageResource(R.drawable.net_error)
                    binding.placeholderText.text = getString(R.string.net_error)
                    binding.buttonUpdate.isVisible = true
                    binding.buttonUpdate.setOnClickListener {
                        searchViewModel.search(binding.inputEditText.text.toString())
                    }
                    binding.searchHistory.isVisible = false
                    binding.clearSearchHistory.isVisible = false
                }

                else -> {
                    Log.d(TAG, "handleUiState: Unhandled UiState: $state")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleUiState: ${e.message}")
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized) {
            outState.putString(SEARCH_ITEM, binding.inputEditText.text.toString())
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null && this::binding.isInitialized) {
            val searchText = savedInstanceState.getString(SEARCH_ITEM, "")
            binding.inputEditText.setText(searchText)
            if (!searchText.isNullOrEmpty()) {
                searchViewModel.search(searchText)
            }
        }
    }

    private companion object {
        private const val SEARCH_ITEM = "SEARCH_ITEM"
    }
}
