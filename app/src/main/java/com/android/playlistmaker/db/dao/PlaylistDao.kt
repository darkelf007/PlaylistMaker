package com.android.playlistmaker.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.playlistmaker.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity?

    @Query("DELETE FROM playlist_table")
    suspend fun clearTable()

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

}