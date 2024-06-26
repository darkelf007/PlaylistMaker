package com.android.playlistmaker.main.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.R
import com.android.playlistmaker.search.presentation.ui.SearchActivity
import com.android.playlistmaker.settings.ui.SettingsActivity

class MainViewModel(private val context: Context) : ViewModel() {

    class MainViewModel(private val context: Context) : ViewModel() {

        fun onSearchClick() {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }

        fun onMediaClick() {
            Toast.makeText(context, "Media!", Toast.LENGTH_SHORT).show()
        }

        fun onSettingsClick() {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}