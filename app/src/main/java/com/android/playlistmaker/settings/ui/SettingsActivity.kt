package com.android.playlistmaker.settings.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.databinding.ActivitySettingsBinding
import com.android.playlistmaker.domain.app.App

import com.android.playlistmaker.settings.util.IntentUtils

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("AAA", "Activity created")

        val app = application as App
        val factory = SettingsViewModelFactory(
            app.settingsRepository,
            this,
            IntentUtils
        )
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)

        viewModel.darkThemeEnabled.observe(this) { enabled ->
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }



        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
        binding.switchTheme.isChecked = viewModel.darkThemeEnabled.value ?: false
    }

    private fun setupObservers() {
        viewModel.darkThemeEnabled.observe(this) { enabled ->
            if (binding.switchTheme.isChecked != enabled) {
                binding.switchTheme.isChecked = enabled
            }
        }
    }

    private fun setupListeners() {
        binding.buttonBackToMain.setOnClickListener {
            finish()
        }
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
        binding.layoutTextWriteToSupport.setOnClickListener {
            val supportEmailIntent = viewModel.getSupportEmailIntent()
            startActivity(supportEmailIntent)
        }
        binding.layoutTextWriteToSupport.setOnClickListener {
            val supportEmailIntent = viewModel.getSupportEmailIntent()
            startActivity(supportEmailIntent)
        }
        binding.layoutToShare.setOnClickListener {
            val shareIntent = viewModel.getShareIntent()
            startActivity(shareIntent)
        }
    }


}
