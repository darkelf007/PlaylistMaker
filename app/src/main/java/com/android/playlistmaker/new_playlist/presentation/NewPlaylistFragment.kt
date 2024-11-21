package com.android.playlistmaker.new_playlist.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentNewplaylistBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var bottomNavigationListener: BottomNavigationListener? = null
    private var imageIsLoaded = false
    private var uriOfImage: Uri? = null

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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkForDialogOutput()
                }

            })

        val backArrowImageView = binding.backArrowNewPlaylist
        val loadImageImageView = binding.loadImageImageview
        val newPlayListButton = binding.newPlaylistButton

        newPlayListButton.isEnabled = false

        binding.editNameNewPlaylist.doOnTextChanged { text, _, _, _ ->
            enableNewPlaylistButton(text.toString())
        }

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
            val name = binding.editNameNewPlaylist.text.toString()
            val description = binding.editDescriptionNewPlaylist.text.toString()

            viewModel.createPlaylist(name, description, uriOfImage)
        }

        viewModel.creationStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                is NewPlaylistFragmentViewModel.CreationStatus.Success -> {
                    val toastPhrase =
                        getString(R.string.add_playlist) + " ${binding.editNameNewPlaylist.text}"
                    Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        })
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

    private fun checkForDialogOutput() {
        if (imageIsLoaded || binding.editNameNewPlaylist.text.toString().isNotEmpty() ||
            binding.editDescriptionNewPlaylist.text.toString().isNotEmpty()
        ) {
            showDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
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

