package com.android.playlistmaker.search.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentSearchBinding
import com.android.playlistmaker.search.presentation.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackAdapterHistory: TrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateUiState(SearchViewModel.UiState.Idle)

        setupAdapters()
        setupRecyclerViews()
        setupObservers()
        setupListeners()

        val query = viewModel.currentQuery.value ?: ""
        if (query.isNotEmpty()) {
            binding.inputEditText.setText(query)
            binding.inputEditText.setSelection(query.length)
            viewModel.updateQuery(query, binding.inputEditText.hasFocus())
        }

    }


    private fun setupAdapters() {
        trackAdapter = TrackAdapter(mutableListOf(), resources)
        trackAdapter.itemClickListener = { track ->
            viewModel.onTrackClick(track)
        }

        trackAdapterHistory = TrackAdapter(mutableListOf(), resources)
        trackAdapterHistory.itemClickListener = { track ->
            viewModel.onTrackClick(track)
        }
    }

    private fun setupRecyclerViews() {
        binding.trackList.apply {
            adapter = trackAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.searchHistoryRecyclerView.apply {
            adapter = trackAdapterHistory
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            handleUiState(state)
        }

        viewModel.navigateToPlayer.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { track ->
                hideKeyboard()
                val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
                findNavController().navigate(action)
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            trackAdapter.updateTracks(tracks)
            binding.trackList.scrollToPosition(0)
        }

        viewModel.searchHistory.observe(viewLifecycleOwner) { tracksHistory ->
            trackAdapterHistory.updateTracks(tracksHistory)
        }

        viewModel.showHistory.observe(viewLifecycleOwner) { shouldShow ->
            binding.searchHistory.isVisible = shouldShow
            binding.clearSearchHistory.isVisible = shouldShow
        }
    }

    private fun setupListeners() {
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            hideKeyboard()
            viewModel.clearTracks()
        }

        binding.inputEditText.apply {
            setOnFocusChangeListener { _, hasFocus ->
                viewModel.evaluateShowHistory(hasFocus, binding.inputEditText.text.toString())
                if (!hasFocus) {
                    hideKeyboard()
                }
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val query = text.toString()
                    viewModel.updateQuery(query, hasFocus())
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

            binding.inputEditText.doOnTextChanged { text, _, _, _ ->
                binding.clearIcon.isVisible = !text.isNullOrEmpty()
                val query = text.toString()
                viewModel.updateQuery(query, binding.inputEditText.hasFocus())
            }
        }

        binding.clearSearchHistory.setOnClickListener {
            viewModel.clearSearchHistory()
        }
    }

    private fun handleUiState(state: SearchViewModel.UiState) {
        with(binding) {
            when (state) {
                is SearchViewModel.UiState.Idle -> {
                    downloadIconFrame.isVisible = false
                    trackList.isVisible = false
                    placeholderImage.isVisible = false
                    placeholderText.isVisible = false
                    buttonUpdate.isVisible = false
                    searchHistory.isVisible = false
                }

                is SearchViewModel.UiState.Loading -> {
                    downloadIconFrame.isVisible = true
                    trackList.isVisible = false
                    placeholderImage.isVisible = false
                    placeholderText.isVisible = false
                    buttonUpdate.isVisible = false
                    searchHistory.isVisible = false
                }

                is SearchViewModel.UiState.Success -> {
                    downloadIconFrame.isVisible = false
                    trackList.isVisible = true
                    placeholderImage.isVisible = false
                    placeholderText.isVisible = false
                    buttonUpdate.isVisible = false
                    searchHistory.isVisible = false
                }

                is SearchViewModel.UiState.History -> {
                    downloadIconFrame.isVisible = false
                    trackList.isVisible = false
                    placeholderImage.isVisible = false
                    placeholderText.isVisible = false
                    buttonUpdate.isVisible = false
                    searchHistory.isVisible =
                        state.showHistory && viewModel.searchHistory.value?.isNotEmpty() == true
                    clearSearchHistory.isVisible = searchHistory.isVisible
                }

                is SearchViewModel.UiState.Empty -> {
                    downloadIconFrame.isVisible = false
                    trackList.isVisible = false
                    placeholderImage.isVisible = true
                    placeholderText.isVisible = true
                    placeholderImage.setImageResource(R.drawable.not_found)
                    placeholderText.text = getString(R.string.not_found)
                    buttonUpdate.isVisible = false
                    searchHistory.isVisible = false
                }

                is SearchViewModel.UiState.Error -> {
                    downloadIconFrame.isVisible = false
                    trackList.isVisible = false
                    placeholderImage.isVisible = true
                    placeholderText.isVisible = true
                    placeholderImage.setImageResource(R.drawable.net_error)
                    placeholderText.text = getString(R.string.net_error)
                    buttonUpdate.isVisible = true

                    buttonUpdate.setOnClickListener {
                        val query = inputEditText.text.toString()
                        viewModel.search(query)
                    }

                    searchHistory.isVisible = false
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = requireActivity().currentFocus
        view?.let {
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.updateQuery("", false)
    }
}
