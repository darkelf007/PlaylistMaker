package com.android.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.playlistmaker.db.dao.PlaylistDao
import com.android.playlistmaker.db.dao.PlaylistTrackDao
import com.android.playlistmaker.db.dao.TrackDao
import com.android.playlistmaker.db.entity.DBTrackEntity
import com.android.playlistmaker.db.entity.PlaylistEntity
import com.android.playlistmaker.db.entity.PlaylistTrackEntity

@Database(
    version = 13,
    entities = [DBTrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistTrackDao(): PlaylistTrackDao
}