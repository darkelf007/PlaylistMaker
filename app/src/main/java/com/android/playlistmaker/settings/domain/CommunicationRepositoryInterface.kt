package com.android.playlistmaker.settings.domain


interface CommunicationRepositoryInterface {
    fun getSupportEmailDetails(): EmailDetails
    fun getUserAgreementUrl(): String
    fun getShareMessage(): String
}