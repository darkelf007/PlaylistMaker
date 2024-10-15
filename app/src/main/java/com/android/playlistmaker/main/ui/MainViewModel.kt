package com.android.playlistmaker.main.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.main.domain.MainInteractor

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {

    init {
        Log.e("MainViewModel", "VM created")
    }

    fun onSearchClick() {
        interactor.onSearchClick()
    }

    fun onMediaClick() {
        interactor.onMediaClick()
    }

    fun onSettingsClick() {
        interactor.onSettingsClick()
    }

    override fun onCleared() {
        Log.e("MainViewModel", "VM cleared")
        super.onCleared()
    }
}
