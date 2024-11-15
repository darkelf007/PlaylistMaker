package com.android.playlistmaker.new_playlist.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentNewplaylistBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.android.playlistmaker.new_playlist.domain.models.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null


    private var imageIsLoaded = false

    private var uriOfImage: Uri? = null

    private lateinit var backArrowImageView: ImageView
    private lateinit var loadImageImageView: ImageView
    private lateinit var editNameEditText: TextInputEditText
    private lateinit var editDescriptionEditText: TextInputEditText
    private lateinit var newPlayListButton: Button


    private val viewModel: NewPlaylistFragmentViewModel by viewModel()


    private lateinit var binding: FragmentNewplaylistBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BottomNavigationListener) {
            bottomNavigationListener = context
        } else {
            throw IllegalArgumentException("Activity must implement BottomNavigationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewplaylistBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                checkForDialogOutput()
            }

        })

        backArrowImageView = binding.backArrowNewPlaylist
        loadImageImageView = binding.loadImageImageview
        editNameEditText = binding.editNameNewPlaylist
        editDescriptionEditText = binding.editDescriptionNewPlaylist
        newPlayListButton = binding.newPlaylistButton

        newPlayListButton.isEnabled = false

        editNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                enableNewPlaylistButton(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }

        })

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    loadImageImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    loadImageImageView.setImageURI(uri)
                    imageIsLoaded = true
                    uriOfImage = uri
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        backArrowImageView.setOnClickListener {
            checkForDialogOutput()
        }

        loadImageImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        newPlayListButton.setOnClickListener {

            val name = editNameEditText.text.toString()

            val filepath =
                if (uriOfImage != null) viewModel.getNameForFile(editNameEditText.text.toString()) else ""

            val playlist = Playlist(
                name = name,
                description = editDescriptionEditText.text.toString(),
                filePath = filepath,
                listOfTracksId = "",
                amountOfTracks = 0
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.insertPlaylistToDatabase(playlist)
            }

            uriOfImage?.let { saveImageToPrivateStorage(uri = it, nameOfFile = filepath) }

            val toastPhrase = getString(R.string.add_playlist) + " $name"

            Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }

    }


    override fun onResume() {
        super.onResume()
        hideBottomNavigation(true)
    }

    override fun onStop() {
        super.onStop()
        hideBottomNavigation(false)
    }

    private fun hideBottomNavigation(isHide: Boolean) {
        bottomNavigationListener?.toggleBottomNavigationViewVisibility(!isHide)
    }

    private fun enableNewPlaylistButton(text: String?) {
        newPlayListButton.isEnabled = !text.isNullOrEmpty()
    }

    private fun checkForDialogOutput() {
        if (imageIsLoaded || editNameEditText.text.toString()
                .isNotEmpty() || editDescriptionEditText.text.toString().isNotEmpty()
        ) {
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.complete_playlist))
            .setMessage(getString(R.string.data_lost))
            .setNeutralButton(getString(R.string.cancel)) { dialog, which ->

            }.setPositiveButton(getString(R.string.complete)) { dialog, which ->
                findNavController().navigateUp()
            }.show()
    }

    private fun saveImageToPrivateStorage(uri: Uri, nameOfFile: String) {
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, nameOfFile)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

    }


}