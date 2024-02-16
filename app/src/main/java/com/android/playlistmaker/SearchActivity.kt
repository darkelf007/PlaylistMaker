package com.android.playlistmaker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val ITunesApiBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder().baseUrl(ITunesApiBaseUrl)
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val ITunesService = retrofit.create(iTunesAPI::class.java)

    private var searchText: String = ""

    private val tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var updateButton: Button
    private lateinit var inputEditText: EditText
    private lateinit var downloadIcon: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        //-------------------------------------------------------------------------------------------------------------------------------
        trackAdapter = TrackAdapter(tracks, resources)
        placeholderImage = findViewById(R.id.placeholderImage)
        placeholderText = findViewById(R.id.placeholderText)
        updateButton = findViewById(R.id.buttonUpdate)
        inputEditText = findViewById(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val backButton = findViewById<Button>(R.id.button_backToMain)
        downloadIcon = findViewById(R.id.download_icon_frame)
        recyclerView = findViewById(R.id.track_list)
        recyclerView.adapter = trackAdapter
        //-------------------------------------------------------------------------------------------------------------------------------
        clearButton.setOnClickListener {
            placeholderImage.isVisible = false
            placeholderText.isVisible = false
            updateButton.isVisible = false
            trackAdapter.notifyDataSetChanged()
            inputEditText.setText("")
            hideKeyboard()
            tracks.clear()

        }

        backButton.setOnClickListener {
            finish()
        }

        inputEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
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
                clearButton.isVisible = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun search() {
        if (inputEditText.text.isNotEmpty()) {
            downloadIcon.isVisible = true
            ITunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>, response: Response<TrackResponse>
                    ) {
                        downloadIcon.isVisible = false
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackAdapter.notifyDataSetChanged()
                                tracks.addAll(response.body()?.results!!)
                                recyclerView.isVisible = true
                            } else {
                                showMessage(getString(R.string.not_found))
                            }
                        } else {
                            showMessage(
                                getString(R.string.net_error)
                            )
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        downloadIcon.isVisible = false
                        showMessage(getString(R.string.net_error))
                    }
                })
        }
    }

    private fun showMessage(text: String) {
        if (text.isNotEmpty()) {
            placeholderImage.isVisible = true
            placeholderText.isVisible = true
            downloadIcon.isVisible = false
            trackAdapter.notifyDataSetChanged()
            recyclerView.isVisible = false
            tracks.clear()

            if (text == getString(R.string.not_found)) {
                updateButton.isVisible = false
                placeholderImage.setImageResource(R.drawable.not_found)
                placeholderText.text = text
            } else {
                placeholderImage.setImageResource(R.drawable.net_error)
                placeholderText.text = text
                updateButton.setOnClickListener { search() }
                updateButton.isVisible = true
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_ITEM, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_ITEM, "SEARCH_ITEM")
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private companion object {
        const val SEARCH_ITEM = "SEARCH_ITEM"
    }
}
