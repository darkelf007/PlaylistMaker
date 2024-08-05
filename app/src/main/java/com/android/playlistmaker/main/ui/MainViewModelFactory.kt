package com.android.playlistmaker.main.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.main.data.MainRepositoryImpl
import com.android.playlistmaker.main.domain.MainUseCase

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val repository by lazy(LazyThreadSafetyMode.NONE) {
        MainRepositoryImpl(context)
    }

    private val useCase by lazy(LazyThreadSafetyMode.NONE) {
        MainUseCase(repository)
    }
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(useCase) as T
    }
}
