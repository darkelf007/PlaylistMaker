package com.android.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.android.playlistmaker.domain.model.Track
import com.android.playlistmaker.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchHistoryRepositoryImpl (private val sharedPrefs: SharedPreferences) :
    SearchHistoryRepository {
    companion object {
        const val TRACK = "TRACK"
    }
    override fun clearHistory() {
        sharedPrefs.edit { remove(TRACK) }
    }

    override fun read(): ArrayList<Track>? {
        val json = sharedPrefs.getString(TRACK, null)
        val changeType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, changeType)
    }

    override fun write(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit { putString(TRACK, json) }
    }
}