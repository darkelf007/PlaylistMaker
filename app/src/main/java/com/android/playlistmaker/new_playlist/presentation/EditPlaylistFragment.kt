package com.android.playlistmaker.new_playlist.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.playlistmaker.R
import com.android.playlistmaker.main.listeners.BottomNavigationListener
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

    override fun onResume() {
        super.onResume()
        hideBottomNavigation(true)
    }

    override fun onStop() {
        super.onStop()
        hideBottomNavigation(false)
    }

    private fun hideBottomNavigation(isHide: Boolean) {
        (requireActivity() as BottomNavigationListener).toggleBottomNavigationViewVisibility(!isHide)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.text = getString(R.string.save_button_edit)

        binding.newPlaylistHeader.text = getString(R.string.edit_current_playlist)


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

    override fun getButtonText(): String {
        return getString(R.string.save_button_edit)
    }

    override fun getHeaderText(): String {
        return getString(R.string.edit_current_playlist)
    }

    override fun setupObserver() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            playlist?.let {
                updateUIWithPlaylist(it)
            }
        }
    }

    private fun updateUIWithPlaylist(playlist: Playlist) {
        binding.editNameNewPlaylist.setText(playlist.name)
        binding.editDescriptionNewPlaylist.setText(playlist.description)

        if (playlist.filePath.isNotEmpty()) {
            val uri = getValidUri(playlist.filePath)
            if (uri != null) {
                binding.loadImageImageview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.loadImageImageview.setImageURI(uri)
            } else {
                binding.loadImageImageview.setImageResource(R.drawable.placeholder_playlist)
            }
        } else {
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
        return if (file.exists()) Uri.fromFile(file) else null
    }


    override fun handleAction(name: String, description: String, uri: Uri?) {
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
                    myAlbumDir.mkdirs()
                }


                deleteOldFile(currentPlaylist?.filePath)

                val file = File(myAlbumDir, getNameForFile(name))

                saveImageToFile(it, file)

                "myalbum/${file.name}"
            }
        )
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
        requireContext().contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
    }

    private fun deleteOldFile(filePath: String?) {
        if (!filePath.isNullOrEmpty()) {
            val file = File(
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                filePath
            )
            if (file.exists()) {
                file.delete()
            }
        }
    }
}





