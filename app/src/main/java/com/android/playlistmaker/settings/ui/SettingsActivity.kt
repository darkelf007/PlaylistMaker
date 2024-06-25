package com.android.playlistmaker.settings.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivitySettingsBinding
import com.android.playlistmaker.domain.app.App
import com.android.playlistmaker.settings.util.IntentUtils

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupListeners()
    }
    private fun setupViewModel() {
        val factory = SettingsViewModelFactory((application as App).settingsRepository)
        viewModel = ViewModelProvider(this, factory).get(SettingsViewModel::class.java)
        viewModel.darkThemeEnabled.observe(this) { enabled ->
            binding.switchTheme.isChecked = enabled
        }
    }

    private fun setupListeners() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }
        binding.buttonBackToMain.setOnClickListener {
            finish()
        }
        binding.layoutToShare.setOnClickListener {
            showShareDialog()
        }
        binding.layoutTextWriteToSupport.setOnClickListener {
            sendSupportEmail()
        }
        binding.layoutUserAgreement.setOnClickListener {
            showUserAgreement()
        }
    }

    private fun showUserAgreement() {
        val userAgreementPath = getString(R.string.user_agreement_url)
        val browseIntent = IntentUtils.createWebIntent(userAgreementPath)
        startActivity(browseIntent)
    }

    private fun sendSupportEmail() {
        val message = getString(R.string.support_message)
        val subject = getString(R.string.support_subject)
        val emails = arrayOf(getString(R.string.email_student))
        if (emails.isNotEmpty()) {
            val email = emails[0]
            val intent = IntentUtils.createEmailIntent(email, subject, message)
            startActivity(intent)
        }
    }

    private fun showShareDialog() {
        val message = getString(R.string.share_Dialog)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(shareIntent, ""))
    }
}
