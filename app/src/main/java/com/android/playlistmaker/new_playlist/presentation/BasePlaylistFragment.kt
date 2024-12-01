package com.android.playlistmaker.new_playlist.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentNewplaylistBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BasePlaylistFragment : Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null
    private var imageIsLoaded = false
    var uriOfImage: Uri? = null

    lateinit var binding: FragmentNewplaylistBinding

    private val pickMediaLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.loadImageImageview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.loadImageImageview.setImageURI(uri)
                imageIsLoaded = true
                uriOfImage = uri
            }
        }

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

    abstract fun getButtonText(): String
    abstract fun getHeaderText(): String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.newPlaylistButton.text = getButtonText()
        binding.newPlaylistHeader.text = getHeaderText()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkForDialogOutput()
                }
            }
        )

        binding.editNameNewPlaylist.doOnTextChanged { text, _, _, _ ->
            enableNewPlaylistButton(text.toString())
        }

        enableNewPlaylistButton(binding.editNameNewPlaylist.text.toString())

        binding.backArrowNewPlaylist.setOnClickListener {
            checkForDialogOutput()
        }

        binding.loadImageImageview.setOnClickListener {
            pickMedia()
        }

        binding.newPlaylistButton.setOnClickListener {
            handleAction(
                binding.editNameNewPlaylist.text.toString(),
                binding.editDescriptionNewPlaylist.text.toString(),
                uriOfImage

            )
        }
        setupObserver()

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
        val isNotBlank = !text.isNullOrBlank()
        binding.newPlaylistButton.isEnabled = isNotBlank
    }

    fun checkForDialogOutput() {
        if (imageIsLoaded || binding.editNameNewPlaylist.text.toString().isNotEmpty() ||
            binding.editDescriptionNewPlaylist.text.toString().isNotEmpty()
        ) {
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    fun pickMedia() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showDialog() {
        Handler(Looper.getMainLooper()).post {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.complete_playlist))
                .setMessage(getString(R.string.data_lost))
                .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(getString(R.string.complete)) { dialog, _ ->
                    findNavController().navigateUp()
                }
                .show()
        }
    }

    abstract fun setupObserver()

    abstract fun handleAction(name: String, description: String, uri: Uri?)
}

