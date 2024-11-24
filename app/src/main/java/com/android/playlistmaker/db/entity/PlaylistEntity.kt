package com.android.playlistmaker.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.playlistmaker.search.domain.SearchTrack

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val filePath: String,
    val listOfTracksId: String = "",
    val amountOfTracks: Int,
    val insertTimeStamp: Long? = 0L
)


fun PlaylistTrackEntity.mapToSearchTrack(): SearchTrack {
    return SearchTrack(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTime?.toIntOrNull() ?: 0,
        artworkUrl100 = this.artworkUrl,
        artworkUrl60 = this.artworkUrl60,
        collectionName = this.collectionName ?: "",
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl,
        insertionTimeStamp = this.insertTimeStamp ?: 0L
    )
}