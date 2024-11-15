package com.android.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.playlistmaker.db.dao.TrackDao
import com.android.playlistmaker.db.entity.DBTrackEntity

@Database(version = 5, entities = [DBTrackEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}