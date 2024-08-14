package com.android.playlistmaker.settings.data

import android.content.Context
import com.android.playlistmaker.R
import com.android.playlistmaker.settings.domain.CommunicationRepositoryInterface
import com.android.playlistmaker.settings.domain.EmailDetails

class CommunicationRepositoryImpl(private val context: Context) : CommunicationRepositoryInterface {
    override fun getSupportEmailDetails(): EmailDetails {
        return EmailDetails(
            email = context.getString(R.string.email_student),
            subject = context.getString(R.string.support_subject),
            message = context.getString(R.string.support_message)
        )
    }

    override fun getUserAgreementUrl(): String {
        return context.getString(R.string.user_agreement_url)
    }

    override fun getShareMessage(): String {
        return context.getString(R.string.share_dialog)
    }
}
