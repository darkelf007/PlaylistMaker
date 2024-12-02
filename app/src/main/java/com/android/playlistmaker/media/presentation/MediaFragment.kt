package com.android.playlistmaker.media.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.FragmentMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaAdapter(childFragmentManager, lifecycle)

        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = getTabTitle(position)
            }
        tabLayoutMediator.attach()
    }

    override fun onDestroyView() {
        tabLayoutMediator.detach()
        _binding = null
        super.onDestroyView()
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            0 -> getString(R.string.favorite_tracks)
            1 -> getString(R.string.playlists)
            else -> throw IllegalArgumentException("Invalid tab position")
        }
    }


    fun navigateToPlayerFragment(track: com.android.playlistmaker.search.domain.SearchTrack) {
        val action = MediaFragmentDirections.actionMediaFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }


    fun navigateToNewPlaylistFragment() {
        val action = MediaFragmentDirections.actionMediaFragmentToNewPlaylistFragment()
        findNavController().navigate(action)
    }
}
