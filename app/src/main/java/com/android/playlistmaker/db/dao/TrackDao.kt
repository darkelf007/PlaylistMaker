package com.android.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.playlistmaker.db.entity.DBTrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: DBTrackEntity)

    @Delete
    suspend fun deleteTrack(track: DBTrackEntity)

    @Query("SELECT * FROM track_table ORDER BY insertionTimeStamp DESC")
    suspend fun getTracksOrderedByInsertionTime(): List<DBTrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM track_table WHERE trackId = :trackId)")
    suspend fun isTrackFavorite(trackId: Int): Boolean


}