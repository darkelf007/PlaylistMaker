package com.android.playlistmaker.settings.ui


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.playlistmaker.settings.domain.EmailDetails
import com.android.playlistmaker.settings.domain.GetThemeUseCase
import com.android.playlistmaker.settings.domain.SendSupportEmailUseCase
import com.android.playlistmaker.settings.domain.ShowShareDialogUseCase
import com.android.playlistmaker.settings.domain.ShowUserAgreementUseCase
import com.android.playlistmaker.settings.domain.ToggleThemeUseCase

class SettingsViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val toggleThemeUseCase: ToggleThemeUseCase,
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

    fun triggerSupportEmail() {
        _supportEmailTrigger.value = sendSupportEmailUseCase.execute()
    }

    fun triggerUserAgreement() {
        _userAgreementTrigger.value = showUserAgreementUseCase.execute()
    }

    fun triggerShare() {
        _shareTrigger.value = showShareDialogUseCase.execute()
    }

    override fun onCleared() {
        Log.e("AAA", "VM cleared")
        super.onCleared()
    }
}