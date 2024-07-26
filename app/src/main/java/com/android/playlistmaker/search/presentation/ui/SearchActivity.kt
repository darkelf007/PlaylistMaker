package com.android.playlistmaker.search.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivitySearchBinding
import com.android.playlistmaker.domain.model.Track

class SearchActivity : AppCompatActivity() {

    private val TAG = "SearchActivity"
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(application, this)
    }

    private lateinit var binding: ActivitySearchBinding

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable =
        Runnable { searchViewModel.search(binding.inputEditText.text.toString()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("AAA", "Activity created")
        Log.d(TAG, "onCreate called")

        binding.trackList.adapter = searchViewModel.trackAdapter
        binding.searchHistoryRecyclerView.adapter = searchViewModel.trackAdapterHistory

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
        searchViewModel.tracks.observe(this, Observer { tracks ->
            Log.d(TAG, "Tracks updated: ${tracks.size} items")
            searchViewModel.trackAdapter.updateTracks(tracks)
        })

        searchViewModel.searchHistory.observe(this, Observer { tracksHistory ->
            Log.d(TAG, "Search history updated: ${tracksHistory.size} items")
            searchViewModel.trackAdapterHistory.updateTracks(tracksHistory)
        })

        searchViewModel.uiState.observe(this, Observer { state ->
            Log.d(TAG, "UI State changed: $state")
            handleUiState(state)
        })
    }

    private fun setupListeners() {
        binding.clearIcon.setOnClickListener {
            binding.placeholderImage.isVisible = false
            binding.placeholderText.isVisible = false
            binding.buttonUpdate.isVisible = false
            binding.inputEditText.setText("")
            searchViewModel.hideKeyboard(currentFocus ?: View(this))
            searchViewModel.clearTracks()
            searchViewModel.showHistory()
        }

        binding.buttonBackToMain.setOnClickListener {
            finish()
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) searchViewModel.showHistory()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.search(binding.inputEditText.text.toString())
                true
            } else {
                false
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = clearButtonVisibility(s)

                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.clearSearchHistory.setOnClickListener {
            searchViewModel.clearSearchHistory()
        }

        searchViewModel.trackAdapterHistory.itemClickListener = { track ->
            searchViewModel.addTrackToHistory(track)
            intentCreation(track)
        }

        searchViewModel.trackAdapter.itemClickListener = { track ->
            searchViewModel.addTrackToHistory(track)
            intentCreation(track)
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
                    binding.placeholderText.text = state.message
                    binding.buttonUpdate.isVisible = true
                    binding.buttonUpdate.setOnClickListener { searchViewModel.search(binding.inputEditText.text.toString()) }
                    binding.searchHistory.isVisible = false
                    binding.clearSearchHistory.isVisible = false
                }

                is SearchViewModel.UiState.HistoryVisible -> {
                    Log.d(TAG, "handleUiState: HistoryVisible")
                    binding.searchHistory.isVisible =
                        searchViewModel.trackAdapterHistory.itemCount > 0
                    binding.clearSearchHistory.isVisible =
                        searchViewModel.trackAdapterHistory.itemCount > 0
                    binding.trackList.isVisible = false
                    binding.placeholderImage.isVisible = false
                    binding.placeholderText.isVisible = false
                    binding.buttonUpdate.isVisible = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in handleUiState: ${e.message}")
        }
    }

    private fun intentCreation(track: Track) {
        if (clickDebounce()) {
            val playerIntent = searchViewModel.createIntentForTrack(track)
            startActivity(playerIntent)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_ITEM, binding.inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_ITEM, "")
        binding.inputEditText.setText(searchText)
        if (searchText.isNullOrEmpty()) {
            searchViewModel.showHistory()
        }
    }

    private companion object {
        private const val SEARCH_ITEM = "SEARCH_ITEM"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}
