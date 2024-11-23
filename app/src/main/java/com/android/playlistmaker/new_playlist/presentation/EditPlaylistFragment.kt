package com.android.playlistmaker.new_playlist.presentation

import android.net.Uri
import com.android.playlistmaker.new_playlist.presentation.viewmodel.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : BasePlaylistFragment() {

    private val viewModel: EditPlaylistViewModel by viewModel()

    override fun setupObserver() {
    }

    override fun handleAction(name: String, description: String, uri: Uri?) {

    }
}
