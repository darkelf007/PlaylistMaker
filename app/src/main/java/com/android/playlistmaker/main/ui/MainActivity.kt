package com.android.playlistmaker.main.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("AAA", "Activity created")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonSearch.setOnClickListener {
            viewModel.onSearchClick()
        }

        binding.buttonMedia.setOnClickListener {
            viewModel.onMediaClick()
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.onSettingsClick()
        }
    }
}
