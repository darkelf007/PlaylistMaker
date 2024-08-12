package com.android.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.playlistmaker.databinding.FavoriteFragmentBinding

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment().apply { }
    }
    private val viewModel: FavoriteFragmentViewModel by this.viewModels()

    private lateinit var binding: FavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}