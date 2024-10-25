package com.android.playlistmaker.search.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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


class SearchFragment : Fragment() {


    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trackAdapter = TrackAdapter(mutableListOf(), resources)
        trackAdapterHistory = TrackAdapter(mutableListOf(), resources)

        binding.trackList.adapter = trackAdapter
        binding.trackList.layoutManager = LinearLayoutManager(context)

        binding.searchHistoryRecyclerView.adapter = trackAdapterHistory
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(context)

        setupObservers()
        setupListeners()

        searchViewModel.resetUiState()

        searchViewModel.currentQuery.value?.let { query ->
            if (binding.inputEditText.text.toString() != query) {
                binding.inputEditText.setText(query)
                binding.inputEditText.setSelection(query.length)
                searchViewModel.updateQuery(query)
            }
        }
    }

    private fun setupObservers() {
        searchViewModel.uiState.observe(viewLifecycleOwner, Observer { state ->
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

        searchViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            trackAdapter.updateTracks(tracks)
        })

        searchViewModel.searchHistory.observe(viewLifecycleOwner, Observer { tracksHistory ->
            trackAdapterHistory.updateTracks(tracksHistory)
        })
    }

    private fun setupListeners() {
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            hideKeyboard()
            searchViewModel.clearTracks()
            searchViewModel.showHistory()
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchViewModel.currentQuery.value.isNullOrEmpty()) {
                searchViewModel.showHistory()
            } else {
                if (!hasFocus) {
                    hideKeyboard()
                }
            }
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.inputEditText.text.toString()
                searchViewModel.updateQuery(query)
                hideKeyboard()
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
        when (state) {
            is SearchViewModel.UiState.Idle -> {
                binding.downloadIconFrame.isVisible = false
                binding.trackList.isVisible = false
                binding.placeholderImage.isVisible = false
                binding.placeholderText.isVisible = false
                binding.buttonUpdate.isVisible = false
                binding.searchHistory.isVisible = false
            }

            is SearchViewModel.UiState.Loading -> {
                binding.downloadIconFrame.isVisible = true
                binding.trackList.isVisible = false
                binding.placeholderImage.isVisible = false
                binding.placeholderText.isVisible = false
                binding.buttonUpdate.isVisible = false
                binding.searchHistory.isVisible = false
            }

            is SearchViewModel.UiState.Success -> {
                binding.downloadIconFrame.isVisible = false
                binding.trackList.isVisible = true
                binding.placeholderImage.isVisible = false
                binding.placeholderText.isVisible = false
                binding.buttonUpdate.isVisible = false
                binding.searchHistory.isVisible = false
            }

            is SearchViewModel.UiState.History -> {
                binding.downloadIconFrame.isVisible = false
                binding.trackList.isVisible = false
                binding.placeholderImage.isVisible = false
                binding.placeholderText.isVisible = false
                binding.buttonUpdate.isVisible = false
                binding.searchHistory.isVisible = state.showHistory && searchViewModel.searchHistory.value?.isNotEmpty() == true
                binding.clearSearchHistory.isVisible = binding.searchHistory.isVisible
            }

            is SearchViewModel.UiState.Empty -> {
                binding.downloadIconFrame.isVisible = false
                binding.trackList.isVisible = false
                binding.placeholderImage.isVisible = true
                binding.placeholderText.isVisible = true
                binding.placeholderImage.setImageResource(R.drawable.not_found)
                binding.placeholderText.text = getString(R.string.not_found)
                binding.buttonUpdate.isVisible = false
                binding.searchHistory.isVisible = false
            }

            is SearchViewModel.UiState.Error -> {
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
            }
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus
        if (view != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}