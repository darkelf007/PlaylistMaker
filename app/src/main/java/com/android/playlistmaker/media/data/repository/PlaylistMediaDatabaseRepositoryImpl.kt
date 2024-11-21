package com.android.playlistmaker.media.data.repository

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.android.playlistmaker.db.PlaylistDatabase
import com.android.playlistmaker.media.domain.db.PlaylistMediaDatabaseRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.models.mapToPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File


class PlaylistMediaDatabaseRepositoryImpl(
    private val playlistDatabase: PlaylistDatabase,
    private val context: Context
) : PlaylistMediaDatabaseRepository {
    override suspend fun getPlaylistsFromDatabase(): Flow<List<Playlist>> = flow {
        val playlistEntityList = playlistDatabase.playlistDao().getPlaylists()
        val playlists = playlistEntityList.map { entity ->
            val imageUri = getUriOfImageFromStorage(entity.filePath)
            entity.mapToPlaylist(imageUri)
        }
        emit(playlists)
    }

    private fun getUriOfImageFromStorage(fileName: String?): Uri? {
        fileName ?: return null
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val file = File(filePath, fileName)
        return if (file.exists()) file.toUri() else null
    }
}
