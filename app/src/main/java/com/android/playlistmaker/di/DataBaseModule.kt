package com.android.playlistmaker.di

import androidx.room.Room
import com.android.playlistmaker.db.AppDatabase
import com.android.playlistmaker.db.PlaylistDatabase
import com.android.playlistmaker.db.PlaylistTrackDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration().build()
    }

    single<PlaylistDatabase> {
        Room.databaseBuilder(androidContext(), PlaylistDatabase::class.java, "playlist_database.db")
            .fallbackToDestructiveMigration().build()
    }

    single<PlaylistTrackDatabase> {
        Room.databaseBuilder(
            androidContext(), PlaylistTrackDatabase::class.java, "playlist_track_databases.db"
        ).fallbackToDestructiveMigration().build()
    }

}