package com.android.playlistmaker.main.domain

import android.content.Context
import com.android.playlistmaker.main.data.MainRepository

class MainUseCase(private val repository: MainRepository) {
    fun onSearchClick(context: Context) {
        repository.onSearchClick(context)
    }

    fun onMediaClick(context: Context) {
        repository.onMediaClick(context)
    }

    fun onSettingsClick(context: Context) {
        repository.onSettingsClick(context)
    }
}