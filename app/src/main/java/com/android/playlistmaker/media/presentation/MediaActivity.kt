package com.android.playlistmaker.media.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaAdapter(supportFragmentManager, lifecycle)
        tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 ->tab.text = resources.getString(R.string.favorite_tracks)
                    1 ->tab.text = resources.getString(R.string.playlists)
                }
            }
        tabLayoutMediator.attach()

        binding.backButton.setOnClickListener{
            finish()
        }
    }

    override fun onDestroy() {
        tabLayoutMediator.detach()
        super.onDestroy()
    }
}
