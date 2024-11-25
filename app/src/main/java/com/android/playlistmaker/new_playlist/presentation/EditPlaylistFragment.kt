package com.android.playlistmaker.new_playlist.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.playlistmaker.R
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.android.playlistmaker.new_playlist.presentation.viewmodel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditPlaylistFragment : BasePlaylistFragment() {

    private val viewModel: EditPlaylistViewModel by viewModel()
    private val args: EditPlaylistFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.loadPlaylistData(args.playlistId)


        setupObserver()


        binding.newPlaylistButton.setOnClickListener {
            handleAction(
                binding.editNameNewPlaylist.text.toString(),
                binding.editDescriptionNewPlaylist.text.toString(),
                uriOfImage
            )
            findNavController().navigateUp()
        }

        binding.loadImageImageview.setOnClickListener {
            pickMedia()
        }

        binding.backArrowNewPlaylist.setOnClickListener {
            checkForDialogOutput()
        }

    }


    override fun setupObserver() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                updateUIWithPlaylist(it)
            }
        }
    }

    private fun updateUIWithPlaylist(playlist: Playlist) {
        Log.d("EditPlaylistFragment", "Updating UI with playlist: $playlist")
        binding.editNameNewPlaylist.setText(playlist.name)
        binding.editDescriptionNewPlaylist.setText(playlist.description)

        if (playlist.filePath.isNotEmpty()) {
            val uri = getValidUri(playlist.filePath)
            if (uri != null) {
                Log.d("EditPlaylistFragment", "Resolved URI: $uri")
                binding.loadImageImageview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.loadImageImageview.setImageURI(uri)
            } else {
                Log.d("EditPlaylistFragment", "File does not exist: ${playlist.filePath}")
                binding.loadImageImageview.setImageResource(R.drawable.placeholder_playlist)
            }
        } else {
            Log.d("EditPlaylistFragment", "No image file path found")
            binding.loadImageImageview.setImageResource(R.drawable.placeholder_playlist)
        }
    }


    private fun getValidUri(filePath: String): Uri? {
        val validFilePath = if (filePath.startsWith("myalbum/")) {
            filePath
        } else {
            "myalbum/$filePath"
        }

        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            validFilePath
        )
        Log.d(
            "EditPlaylistFragment",
            "Checking file existence: ${file.absolutePath}, exists: ${file.exists()}"
        )
        return if (file.exists()) Uri.fromFile(file) else null
    }


    override fun handleAction(name: String, description: String, uri: Uri?) {
        Log.d("EditPlaylistFragment", "handleAction called with name=$name, description=$description, uri=$uri")

        val currentPlaylist = viewModel.playlist.value

        viewModel.updatePlaylist(
            name = name,
            description = description,
            filePath = uri?.let {
                val myAlbumDir = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "myalbum"
                )
                if (!myAlbumDir.exists()) {
                    Log.d("EditPlaylistFragment", "Directory does not exist, creating: $myAlbumDir")
                    myAlbumDir.mkdirs()
                }


                deleteOldFile(currentPlaylist?.filePath)

                val file = File(myAlbumDir, getNameForFile(name))
                Log.d("EditPlaylistFragment", "Saving new image to: ${file.absolutePath}")

                saveImageToFile(it, file)

                "myalbum/${file.name}"
            }
        )
        Log.d("EditPlaylistFragment", "Updated playlist with new data")
        findNavController().navigateUp()
    }




    private fun getNameForFile(nameOfPlaylist: String): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val result = nameOfPlaylist.replace(" ", "_")
        return "${result}_${formattedDateTime}.jpg"
    }


    private fun saveImageToFile(sourceUri: Uri, destinationFile: File) {
        Log.d("EditPlaylistFragment", "Starting saveImageToFile with sourceUri=$sourceUri, destinationFile=${destinationFile.absolutePath}")
        requireContext().contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val success = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                if (success) {
                    Log.d("EditPlaylistFragment", "Image successfully saved to: ${destinationFile.absolutePath}")
                } else {
                    Log.e("EditPlaylistFragment", "Failed to save image")
                }
            }
        } ?: run {
            Log.e("EditPlaylistFragment", "Failed to open input stream for URI: $sourceUri")
        }
    }

    private fun deleteOldFile(filePath: String?) {
        if (!filePath.isNullOrEmpty()) {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                filePath
            )
            if (file.exists()) {
                val deleted = file.delete()
                Log.d("EditPlaylistFragment", "Old file deleted: $deleted, path: ${file.absolutePath}")
            } else {
                Log.d("EditPlaylistFragment", "File not found for deletion: ${file.absolutePath}")
            }
        } else {
            Log.d("EditPlaylistFragment", "No file path provided for deletion")
        }
    }





}