package com.android.playlistmaker.main.data

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.android.playlistmaker.main.domain.MainRepository
import com.android.playlistmaker.media.presentation.MediaActivity
import com.android.playlistmaker.search.presentation.ui.SearchActivity
import com.android.playlistmaker.settings.ui.SettingsActivity

class MainRepositoryImpl(private val context: Context) : MainRepository {
    override fun onSearchClick() {
        val intent = Intent(context, SearchActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun onMediaClick() {
        val intent = Intent(context, MediaActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
    override fun onSettingsClick() {
        val intent = Intent(context, SettingsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
