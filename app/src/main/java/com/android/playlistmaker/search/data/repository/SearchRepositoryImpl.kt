package com.android.playlistmaker.search.data.repository

import com.android.playlistmaker.search.data.model.TrackDataModel
import com.android.playlistmaker.search.data.model.TrackResponseData
import com.android.playlistmaker.search.domain.SearchRepository
import com.android.playlistmaker.search.domain.SearchTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(private val dataSource: SearchDataSource) : SearchRepository {

    override fun search(query: String): Flow<List<SearchTrack>> = flow {
        val trackResponse: TrackResponseData = dataSource.search(query)
        val tracks = trackResponse.results.map { it.toDomain() }
        emit(tracks)
    }

    private fun TrackDataModel.toDomain(): SearchTrack {
        return SearchTrack(
            trackName = this.trackName,
            artistName = this.artistName,
            trackTimeMillis = this.trackTimeMillis,
            artworkUrl100 = this.artworkUrl100,
            artworkUrl60 = this.artworkUrl60,
            trackId = this.trackId,
            collectionName = this.collectionName,
            releaseDate = this.releaseDate,
            primaryGenreName = this.primaryGenreName,
            country = this.country,
            previewUrl = this.previewUrl
        )
    }
}
