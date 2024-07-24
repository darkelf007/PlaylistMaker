package com.android.playlistmaker.main.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.main.domain.MainUseCase

class MainViewModel(private val useCase: MainUseCase) : ViewModel() {

    fun onSearchClick(context: Context) {
        useCase.onSearchClick(context)
    }

    fun onMediaClick(context: Context) {
        useCase.onMediaClick(context)
    }

    fun onSettingsClick(context: Context) {
        useCase.onSettingsClick(context)
    }
}
