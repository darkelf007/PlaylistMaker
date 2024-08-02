
package com.android.playlistmaker.search.domain


interface SearchRepository {
    suspend fun search(query: String): List<SearchTrack>
}
