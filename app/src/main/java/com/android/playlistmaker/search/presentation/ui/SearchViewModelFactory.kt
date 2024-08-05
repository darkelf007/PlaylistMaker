package com.android.playlistmaker.search.presentation.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.search.data.repository.SearchHistoryDataSourceImpl
import com.android.playlistmaker.search.data.repository.SearchDataSourceImpl
import com.android.playlistmaker.search.domain.SearchHistoryRepository
import com.android.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.android.playlistmaker.search.domain.SearchInteractor
import com.android.playlistmaker.search.domain.SearchRepository
import com.android.playlistmaker.search.data.repository.SearchRepositoryImpl

class SearchViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val sharedPreferences = application.getSharedPreferences("HISTORY_PREFERENCES", Context.MODE_PRIVATE)
        val searchHistoryDataSource = SearchHistoryDataSourceImpl(sharedPreferences)
        val searchHistoryRepository: SearchHistoryRepository = SearchHistoryRepositoryImpl(searchHistoryDataSource)
        val searchDataSource = SearchDataSourceImpl()
        val searchRepository: SearchRepository = SearchRepositoryImpl(searchDataSource)
        val searchInteractor = SearchInteractor(searchRepository, searchHistoryRepository)

        return modelClass.getConstructor(
            Application::class.java,
            SearchInteractor::class.java
        ).newInstance(
            application,
            searchInteractor
        )
    }
}
