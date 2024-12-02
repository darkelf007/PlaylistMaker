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
    val trackTime: Int,
    val amountOfTracks: Int,
    val insertTimeStamp: Long? = 0L
)


fun PlaylistTrackEntity.mapToSearchTrack(): SearchTrack {
    val trackTimeMillis = parseTrackTimeToMillis(trackTime)

    return SearchTrack(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = trackTimeMillis,
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

private fun parseTrackTimeToMillis(trackTime: String?): Int {
    if (trackTime.isNullOrEmpty()) return 0

    val parts = trackTime.split(":")
    if (parts.size != 2) return 0

    val minutes = parts[0].toIntOrNull() ?: 0
    val seconds = parts[1].toIntOrNull() ?: 0

    return (minutes * 60 + seconds) * 1000
}