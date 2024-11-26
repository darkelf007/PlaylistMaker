package com.android.playlistmaker.new_playlist.presentation

import android.net.Uri
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.new_playlist.presentation.viewmodel.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : BasePlaylistFragment() {

    private val viewModel: NewPlaylistViewModel by viewModel()


    override fun setupObserver() {
        viewModel.creationStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is NewPlaylistViewModel.CreationStatus.Success -> {
                    val toastPhrase =
                        getString(R.string.add_playlist) + " ${binding.editNameNewPlaylist.text}"
                    Toast.makeText(requireContext(), toastPhrase, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun handleAction(name: String, description: String, uri: Uri?) {
        viewModel.createPlaylist(name, description, uri)
    }


    override fun getButtonText(): String {
        return getString(R.string.create_new_playlist_text_button)
    }

    override fun getHeaderText(): String {
        return getString(R.string.new_playlist_text)
    }
}