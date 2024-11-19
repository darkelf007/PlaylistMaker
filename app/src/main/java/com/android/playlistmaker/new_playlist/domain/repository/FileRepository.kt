package com.android.playlistmaker.new_playlist.domain.repository

import android.net.Uri

interface FileRepository {
    suspend fun saveImageToPrivateStorage(uri: Uri, nameOfFile: String): Result<Unit>
}


