package com.android.playlistmaker.settings.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.settings.domain.EmailDetails
import com.android.playlistmaker.settings.domain.GetThemeUseCaseInterface
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCaseInterface

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCaseInterface,
    private val toggleThemeUseCase: ToggleThemeUseCaseInterface,
    private val showUserAgreementUseCase: ShowUserAgreementUseCase,
    private val sendSupportEmailUseCase: SendSupportEmailUseCase,
    private val showShareDialogUseCase: ShowShareDialogUseCase
) : ViewModel() {

    private val _darkThemeEnabled = MutableLiveData<Boolean>()
    val darkThemeEnabled: LiveData<Boolean> get() = _darkThemeEnabled

    private val _supportEmailTrigger = MutableLiveData<EmailDetails?>()
    val supportEmailTrigger: LiveData<EmailDetails?> get() = _supportEmailTrigger

    private val _userAgreementTrigger = MutableLiveData<String?>()
    val userAgreementTrigger: LiveData<String?> get() = _userAgreementTrigger

    private val _shareTrigger = MutableLiveData<String?>()
    val shareTrigger: LiveData<String?> get() = _shareTrigger

    init {
        Log.e("SettingsViewModel", "VM created")
        _darkThemeEnabled.value = isDarkThemeEnabled()
    }

    fun switchTheme(isDark: Boolean) {
        Log.d("SettingsViewModel", "Switch theme to: $isDark")
        toggleThemeUseCase.execute(isDark)
        _darkThemeEnabled.value = isDark
    }

    private fun isDarkThemeEnabled(): Boolean {
        Log.d("SettingsViewModel", "Checking if dark theme is enabled")
        return getThemeUseCase.execute()
    }

    fun triggerSupportEmail() {
        Log.d("SettingsViewModel", "Trigger support email")
        _supportEmailTrigger.value = sendSupportEmailUseCase.execute()
    }

    fun triggerUserAgreement() {
        Log.d("SettingsViewModel", "Trigger user agreement")
        _userAgreementTrigger.value = showUserAgreementUseCase.execute()
    }

    fun triggerShare() {
        Log.d("SettingsViewModel", "Trigger share")
        _shareTrigger.value = showShareDialogUseCase.execute()
    }

    fun clearUserAgreementTrigger() {
        _userAgreementTrigger.value = null
    }

    fun clearSupportEmailTrigger() {
        _supportEmailTrigger.value = null
    }

    fun clearShareTrigger() {
        _shareTrigger.value = null
    }

    override fun onCleared() {
        Log.e("SettingsViewModel", "VM cleared")
        super.onCleared()
    }
}