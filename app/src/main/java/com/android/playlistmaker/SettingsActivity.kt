package com.android.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Кнопка назад
        val backButton = findViewById<Button>(R.id.button_backToMain)
        backButton.setOnClickListener {
            finish()
        }
        // Поделиться ссылкой
        val linearLayout: FrameLayout = findViewById(R.id.layoutToShare)
        linearLayout.setOnClickListener {
            showShareDialog()
        }
        // Написать в поддержку
        val supportButton: FrameLayout = findViewById(R.id.layoutTextWriteToSupport)
        supportButton.setOnClickListener {
            sendSupportEmail()
        }
        // Пользовательское соглашение
        val userAgreementButton: FrameLayout = findViewById(R.id.layoutUserAgreement)
        userAgreementButton.setOnClickListener {
            showUserAgreement()
        }

    }

    private fun showUserAgreement() {
        val browseIntent = Intent(Intent.ACTION_VIEW)
        val userAgreementPath = getString(R.string.user_agreement_url)
        browseIntent.data = Uri.parse(userAgreementPath)
        startActivity(browseIntent)
    }

    private fun sendSupportEmail() {
        Intent(Intent.ACTION_SENDTO).apply {
            val message = getString(R.string.support_message)
            val subject = getString(R.string.support_subject)
            val email = arrayOf(getString(R.string.email_student))
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, email)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            startActivity(this)
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
