package com.android.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.playlistmaker.db.dao.PlaylistDao
import com.android.playlistmaker.db.entity.PlaylistEntity

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

}