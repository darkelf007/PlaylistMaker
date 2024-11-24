package com.android.playlistmaker.new_playlist.data.Impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.android.playlistmaker.new_playlist.domain.repository.FileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class FileRepositoryImpl(
    private val context: Context
) : FileRepository {
    override suspend fun saveImageToPrivateStorage(uri: Uri, nameOfFile: String): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val filePath =
                    File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
                if (!filePath.exists()) {
                    filePath.mkdirs()
                }
                val file = File(filePath, nameOfFile)
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                }
            }
            Unit
        }.onFailure {
            Log.e("FileRepositoryImpl", "Failed to save image", it)
        }
    }
}
