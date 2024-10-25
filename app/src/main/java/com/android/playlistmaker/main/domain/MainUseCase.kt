package com.android.playlistmaker.main.domain

class MainUseCase(private val repository: MainRepository) : MainInteractor {
    override fun onSearchClick() {
        repository.onSearchClick()
    }

    override fun onMediaClick() {
        repository.onMediaClick()
    }

    override fun onSettingsClick() {
        repository.onSettingsClick()
    }
}
