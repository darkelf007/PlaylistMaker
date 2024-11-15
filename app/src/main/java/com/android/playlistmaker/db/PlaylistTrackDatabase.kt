package com.android.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.playlistmaker.db.dao.PlaylistTrackDao
import com.android.playlistmaker.db.entity.PlaylistTrackEntity

@Database(version = 1, entities = [PlaylistTrackEntity::class])
abstract class PlaylistTrackDatabase : RoomDatabase() {

    abstract fun playlistTrackDao(): PlaylistTrackDao

}

