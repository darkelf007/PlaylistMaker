package com.android.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.playlistmaker.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_track_table WHERE trackId IN (:ids) ORDER BY insertTimeStamp DESC")
    suspend fun getTracksByListIds(ids: List<Int>): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_track_table WHERE trackId =:id")
    suspend fun deleteTrackById(id: Int)

}