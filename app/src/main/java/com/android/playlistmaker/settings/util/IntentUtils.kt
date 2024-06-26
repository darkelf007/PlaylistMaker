package com.android.playlistmaker.settings.util

import android.content.Intent
import android.net.Uri

object IntentUtils {
    fun createEmailIntent(email: String, subject: String, message: String): Intent =
        Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

    fun createWebIntent(url: String): Intent =
        Intent(Intent.ACTION_VIEW, Uri.parse(url))

    fun createShareIntent(message: String): Intent =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
}
