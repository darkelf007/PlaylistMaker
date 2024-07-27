package com.android.playlistmaker.search.presentation.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.player.presentation.ui.PlayerActivity
import com.android.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.android.playlistmaker.search.presentation.adapter.TrackAdapter
import com.google.gson.Gson


class SearchViewModelFactory(
    private val application: Application,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Application::class.java,
            Context::class.java,
            Gson::class.java,
            SearchHistoryRepositoryImpl::class.java,
            TrackAdapter::class.java,
            TrackAdapter::class.java,
            Class::class.java
        ).newInstance(
            application,
            context,
            Gson(),
            SearchHistoryRepositoryImpl(application.getSharedPreferences("HISTORY_PREFERENCES", Context.MODE_PRIVATE)),
            TrackAdapter(mutableListOf(), context.resources),
            TrackAdapter(mutableListOf(), context.resources),
            PlayerActivity::class.java
        )
    }
}
