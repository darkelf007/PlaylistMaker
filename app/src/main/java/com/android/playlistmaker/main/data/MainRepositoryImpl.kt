package com.android.playlistmaker.main.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.playlistmaker.search.presentation.ui.SearchActivity
import com.android.playlistmaker.settings.ui.SettingsActivity

class MainRepositoryImpl : MainRepository {
    override fun onSearchClick(context: Context) {
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent)
    }

    override fun onMediaClick(context: Context) {
        Toast.makeText(context, "Media!", Toast.LENGTH_SHORT).show()
    }

    override fun onSettingsClick(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }
}