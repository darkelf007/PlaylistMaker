package com.android.playlistmaker.settings.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.App
import com.android.playlistmaker.creator.Creator
import com.android.playlistmaker.databinding.ActivitySettingsBinding
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
            Creator.createSendSupportEmailUseCase(this),
            Creator.createShowUserAgreementUseCase(this),
            Creator.createShowShareDialogUseCase(this)
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
        viewModel.supportEmailTrigger.observe(this) { details ->
            if (details != null) {
                val intent = IntentUtils.createEmailIntent(
                    details.email,
                    details.subject,
                    details.message
                )
                startActivity(intent)
            }
        }
        viewModel.userAgreementTrigger.observe(this) { url ->
            if (url != null) {
                val intent = IntentUtils.createWebIntent(url)
                startActivity(intent)
            }
        }
        viewModel.shareTrigger.observe(this) { message ->
            if (message != null) {
                val intent = IntentUtils.createShareIntent(message)
                startActivity(intent)
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
            viewModel.triggerSupportEmail()
        }
        binding.layoutUserAgreement.setOnClickListener {
            viewModel.triggerUserAgreement()
        }
        binding.layoutToShare.setOnClickListener {
            viewModel.triggerShare()
        }

    }
}
