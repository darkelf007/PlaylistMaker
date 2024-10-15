package com.android.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.widget.SearchView
import android.widget.EditText

class SearchActivity : AppCompatActivity() {

    private lateinit var searchText: EditText
    private var currentText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<Button>(R.id.button_backToMain)
        backButton.setOnClickListener {
            finish()
        }

        val searchView = findViewById<SearchView>(R.id.search_view)
        searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        searchText.hint = "Поиск"
        searchText.maxLines = 1
        searchText.inputType = InputType.TYPE_CLASS_TEXT

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val closeButton = searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn)
                closeButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
                currentText = s.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        if (savedInstanceState != null) {
            currentText = savedInstanceState.getString("searchText", "")
            searchText.setText(currentText)
        }

        searchView.setOnCloseListener {
            searchText.text.clear()
            hideKeyboard()
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", currentText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentText = savedInstanceState.getString("searchText", "")
        searchText.setText(currentText)
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchText.windowToken, 0)
    }
}
