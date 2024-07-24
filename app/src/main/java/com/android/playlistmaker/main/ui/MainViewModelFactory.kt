package com.android.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.main.data.MainRepositoryImpl
import com.android.playlistmaker.main.domain.MainUseCase

class MainViewModelFactory : ViewModelProvider.Factory {

    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        MainRepositoryImpl()
    }

    private val useCase by lazy(LazyThreadSafetyMode.NONE) {
        MainUseCase(repository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
