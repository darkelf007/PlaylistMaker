package com.android.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.android.playlistmaker.databinding.FragmentSettingsBinding
import com.android.playlistmaker.settings.util.IntentUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        settingsViewModel.darkThemeEnabled.observe(viewLifecycleOwner) { enabled ->
            if (binding.switchTheme.isChecked != enabled) {
                binding.switchTheme.isChecked = enabled
            }
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        settingsViewModel.supportEmailTrigger.observe(viewLifecycleOwner) { details ->
            details?.let {
                val intent = IntentUtils.createEmailIntent(it.email, it.subject, it.message)
                startActivity(intent)
                settingsViewModel.clearSupportEmailTrigger()
            }
        }
        settingsViewModel.userAgreementTrigger.observe(viewLifecycleOwner) { url ->
            url?.let {
                val intent = IntentUtils.createWebIntent(it)
                startActivity(intent)
                settingsViewModel.clearUserAgreementTrigger()
            }
        }
        settingsViewModel.shareTrigger.observe(viewLifecycleOwner) { message ->
            message?.let {
                val intent = IntentUtils.createShareIntent(it)
                startActivity(intent)
                settingsViewModel.clearShareTrigger()
            }
        }
    }

    private fun setupListeners() {

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.switchTheme(isChecked)
        }
        binding.layoutTextWriteToSupport.setOnClickListener {
            settingsViewModel.triggerSupportEmail()
        }
        binding.layoutUserAgreement.setOnClickListener {
            settingsViewModel.triggerUserAgreement()
        }
        binding.layoutToShare.setOnClickListener {
            settingsViewModel.triggerShare()
        }
    }
}
