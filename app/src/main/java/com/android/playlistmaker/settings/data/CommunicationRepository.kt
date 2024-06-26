package com.android.playlistmaker.settings.data

import android.content.Context
import com.android.playlistmaker.R
import com.android.playlistmaker.settings.data.model.EmailDetails

class CommunicationRepository(private val context: Context) {

    fun getUserAgreementUrl(): String {
        return context.getString(R.string.user_agreement_url)
    }

    fun getSupportEmailDetails(): EmailDetails {
        return EmailDetails(
            email = context.getString(R.string.email_student),
            subject = context.getString(R.string.support_subject),
            message = context.getString(R.string.support_message)
        )
    }

    fun getShareMessage(): String {
        return context.getString(R.string.share_Dialog)
    }
}