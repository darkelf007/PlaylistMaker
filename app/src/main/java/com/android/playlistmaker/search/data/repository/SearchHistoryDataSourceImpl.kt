package com.android.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.android.playlistmaker.search.domain.SearchTrack
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryDataSourceImpl(private val sharedPrefs: SharedPreferences) : SearchHistoryDataSource {
    companion object {
        const val TRACK = "TRACK"
    }

    override fun clearHistory() {
        sharedPrefs.edit { remove(TRACK) }
    }

    override fun read(): List<SearchTrack>? {
        val json = sharedPrefs.getString(TRACK, null)
        val changeType = object : TypeToken<List<SearchTrack>>() {}.type
        return Gson().fromJson(json, changeType)
    }

    override fun write(searchTracks: List<SearchTrack>) {
        val json = Gson().toJson(searchTracks)
        sharedPrefs.edit { putString(TRACK, json) }
    }
}