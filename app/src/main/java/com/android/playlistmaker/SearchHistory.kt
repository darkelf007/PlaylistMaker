package com.android.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val TRACK = "TRACK"
class SearchHistory (private val sharedPrefs: SharedPreferences) {

    fun clearHistory() {
        sharedPrefs.edit { remove(TRACK) }
    }

    fun read(): ArrayList<Track>? {
        val json = sharedPrefs.getString(TRACK, null)
        val changeType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, changeType)
    }

    fun write(tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPrefs.edit { putString(TRACK, json) }
    }
}