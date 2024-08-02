package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.model.TrackDataModel
import com.android.playlistmaker.search.data.model.TrackResponseData
import com.android.playlistmaker.search.domain.SearchRepository
import com.android.playlistmaker.search.domain.SearchTrack

class SearchRepositoryImpl(private val dataSource: SearchDataSource) : SearchRepository {

    override suspend fun search(query: String): List<SearchTrack> {
        val trackResponse: TrackResponseData = dataSource.search(query)
        return trackResponse.results.map { it.toDomain() }
    }

    private fun TrackDataModel.toDomain(): SearchTrack {
        return SearchTrack(
            trackName = this.trackName,
            artistName = this.artistName,
            trackTime = this.trackTime,
            artworkUrl100 = this.artworkUrl100,
            trackId = this.trackId,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
    }
}
