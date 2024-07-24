package com.android.playlistmaker.settings.ui

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.settings.data.CommunicationRepository
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase
import com.android.playlistmaker.settings.util.IntentUtils

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val toggleThemeUseCase: ToggleThemeUseCase,
    private val showUserAgreementUseCase: ShowUserAgreementUseCase,
    private val sendSupportEmailUseCase: SendSupportEmailUseCase,
    private val showShareDialogUseCase: ShowShareDialogUseCase,
    private val communicationRepository: CommunicationRepository
) : ViewModel() {

    private val _darkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean> get() = _darkThemeEnabled


    init {
        Log.e("AAA", "VM created")
        _darkThemeEnabled.value = isDarkThemeEnabled()
    }

    fun switchTheme(isDark: Boolean) {
        toggleThemeUseCase.execute(isDark)
        _darkThemeEnabled.value = isDark
    }


    private fun isDarkThemeEnabled(): Boolean {
        return getThemeUseCase.execute()
    }

    fun getUserAgreementIntent(): Intent {
        val url = communicationRepository.getUserAgreementUrl()
        return IntentUtils.createWebIntent(url)
    }

    fun getSupportEmailIntent(): Intent {
        val emailDetails = communicationRepository.getSupportEmailDetails()
        return IntentUtils.createEmailIntent(emailDetails.email, emailDetails.subject, emailDetails.message)
    }

    fun getShareIntent(): Intent {
        val message = communicationRepository.getShareMessage()
        return IntentUtils.createShareIntent(message)
    }

    override fun onCleared() {
        Log.e("AAA", "VM cleared")
        super.onCleared()
    }
}