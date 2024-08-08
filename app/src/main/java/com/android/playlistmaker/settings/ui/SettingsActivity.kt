package com.android.playlistmaker.settings.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.playlistmaker.databinding.ActivitySettingsBinding
import com.android.playlistmaker.settings.util.IntentUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SettingsActivity", "Activity created")

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()

        if (savedInstanceState != null) {
            binding.switchTheme.isChecked = savedInstanceState.getBoolean("DARK_THEME", false)
        } else {
            binding.switchTheme.isChecked = settingsViewModel.darkThemeEnabled.value ?: false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("DARK_THEME", binding.switchTheme.isChecked)
    }

    private fun setupObservers() {
        settingsViewModel.darkThemeEnabled.observe(this) { enabled ->
            Log.d("SettingsActivity", "Dark theme enabled: $enabled")
            if (binding.switchTheme.isChecked != enabled) {
                binding.switchTheme.isChecked = enabled
            }
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        settingsViewModel.supportEmailTrigger.observe(this) { details ->
            details?.let {
                Log.d("SettingsActivity", "Support email triggered")
                val intent = IntentUtils.createEmailIntent(it.email, it.subject, it.message)
                startActivity(intent)
                settingsViewModel.clearSupportEmailTrigger()
            }
        }
        settingsViewModel.userAgreementTrigger.observe(this) { url ->
            Log.d("SettingsActivity", "User agreement trigger observer")
            url?.let {
                Log.d("SettingsActivity", "User agreement triggered")
                val intent = IntentUtils.createWebIntent(it)
                startActivity(intent)
                settingsViewModel.clearUserAgreementTrigger()
            }
        }
        settingsViewModel.shareTrigger.observe(this) { message ->
            message?.let {
                Log.d("SettingsActivity", "Share triggered")
                val intent = IntentUtils.createShareIntent(it)
                startActivity(intent)
                settingsViewModel.clearShareTrigger()
            }
        }
    }

    private fun setupListeners() {
        Log.d("SettingsActivity", "Setting up listeners")
        binding.buttonBackToMain.setOnClickListener {
            Log.d("SettingsActivity", "Back to main clicked")
            finish()
        }
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            Log.d("SettingsActivity", "Switch theme clicked: $isChecked")
            settingsViewModel.switchTheme(isChecked)
        }
        binding.layoutTextWriteToSupport.setOnClickListener {
            Log.d("SettingsActivity", "Write to support clicked")
            settingsViewModel.triggerSupportEmail()
        }
        binding.layoutUserAgreement.setOnClickListener {
            Log.d("SettingsActivity", "User agreement clicked")
            settingsViewModel.triggerUserAgreement()
        }
        binding.layoutToShare.setOnClickListener {
            Log.d("SettingsActivity", "Share clicked")
            settingsViewModel.triggerShare()
        }

    }
}
