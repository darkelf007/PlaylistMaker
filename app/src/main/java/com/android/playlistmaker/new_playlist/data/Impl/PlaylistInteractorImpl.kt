package com.android.playlistmaker.new_playlist.data.Impl

import android.net.Uri
import com.android.playlistmaker.new_playlist.domain.db.PlaylistInteractor
import com.android.playlistmaker.new_playlist.domain.db.PlaylistRepository
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.domain.repository.FileRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val fileRepository: FileRepository
) : PlaylistInteractor {
    override suspend fun addPlaylist(name: String, description: String, uriOfImage: Uri?) {
        val trimmedName = name.trim()
        val filePath = if (uriOfImage != null) getNameForFile(trimmedName) else ""
        val playlist = Playlist(
            name = trimmedName,
            description = description.trim(),
            filePath = filePath,
            listOfTracksId = "",
            amountOfTracks = 0
        )
        playlistRepository.addPlaylist(playlist)
        if (uriOfImage != null && filePath.isNotEmpty()) {
            fileRepository.saveImageToPrivateStorage(uriOfImage, filePath)
        }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Long): Playlist? {
        return playlistRepository.getPlaylistById(id)
    }

    private fun getNameForFile(nameOfPlaylist: String): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val result = nameOfPlaylist.replace(" ", "_")
        return "${result}_${formattedDateTime}.jpg"
    }
}