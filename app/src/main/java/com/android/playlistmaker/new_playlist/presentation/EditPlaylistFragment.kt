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
            }
        } else {
            Log.d("EditPlaylistFragment", "No image file path found")
        }
    }

    private fun getValidUri(filePath: String): Uri? {
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            filePath
        )
        Log.d(
            "PlaylistInfoFragment",
            "Checking file existence: ${file.absolutePath}, exists: ${file.exists()}"
        )
        return if (file.exists()) Uri.fromFile(file) else null
    }

    override fun handleAction(name: String, description: String, uri: Uri?) {
        viewModel.updatePlaylist(
            name = name,
            description = description,
            uri = uri?.let {
                val myAlbumDir = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "myalbum"
                )
                if (!myAlbumDir.exists()) {
                    myAlbumDir.mkdirs()
                }
                val filePath = File(myAlbumDir, getNameForFile(name))
                saveImageToFile(it, filePath)
                Uri.fromFile(File("myalbum/${filePath.name}"))
            }
        )
    }


    private fun getNameForFile(nameOfPlaylist: String): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val result = nameOfPlaylist.replace(" ", "_")
        return "${result}_${formattedDateTime}.jpg"
    }


    private fun saveImageToFile(sourceUri: Uri, destinationFile: File) {
        requireContext().contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
    }

}