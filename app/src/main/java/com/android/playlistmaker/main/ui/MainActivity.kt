// In MainActivity.kt
package com.android.playlistmaker.main.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("AAA", "Activity created")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonSearch.setOnClickListener {
            viewModel.onSearchClick(this)
        }

        binding.buttonMedia.setOnClickListener {
            viewModel.onMediaClick(this)
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.onSettingsClick(this)
        }
    }
}
