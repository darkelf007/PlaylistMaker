package com.android.playlistmaker.main.data

import android.content.Context

interface MainRepository {
    fun onSearchClick(context: Context)
    fun onMediaClick(context: Context)
    fun onSettingsClick(context: Context)
}