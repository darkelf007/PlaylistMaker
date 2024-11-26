package com.android.playlistmaker.db.entity

import android.util.Log
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
    val trackTimeMillis = try {
        trackTime?.let {
            val parts = it.split(":").map { part -> part.toIntOrNull() ?: 0 }
            if (parts.size == 2) {
                (parts[0] * 60 + parts[1]) * 1000
            } else {
                0
            }
        } ?: 0
    } catch (e: Exception) {
        Log.e("Mapping", "Error parsing track time: $trackTime", e)
        0
    }

    Log.d("Mapping", "Mapping track ID: $trackId, trackTime (raw): $trackTime, trackTimeMillis (parsed): $trackTimeMillis")

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
