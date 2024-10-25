package com.android.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.android.playlistmaker.main.domain.MainInteractor

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {


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
        super.onCleared()
    }
}
