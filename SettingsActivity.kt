package com.android.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    private fun showUserAgreement() {
        val message = "Страничка пользовательского соглашения https://yandex.ru/legal/practicum_offer/"

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)

        startActivity(Intent.createChooser(shareIntent, ""))
    }

    private fun sendSupportEmail() {
        val studentEmail = "dtalievanar@gmail.com"
        val subject = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        val body = "Спасибо разработчикам и разработчицам за крутое приложение!"

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(studentEmail))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, ""))
        } else {
            Toast.makeText(this, "Почтовый клиент не найден", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showShareDialog() {
        val message = "Привет, посетите нашу страничку: https://practicum.yandex.ru/android-developer/?from=catalog"

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)

        startActivity(Intent.createChooser(shareIntent, ""))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<Button>(R.id.button_backToMain)
        backButton.setOnClickListener {
            finish()
        }
        val linearLayout: LinearLayout = findViewById(R.id.layoutToShare)
        linearLayout.setOnClickListener {
            showShareDialog()
        }
        val supportButton: LinearLayout = findViewById(R.id.layoutTextWriteToSupport)
        supportButton.setOnClickListener {
            sendSupportEmail()
        }
        val userAgreementButton: LinearLayout = findViewById(R.id.layoutUserAgreement)
        userAgreementButton.setOnClickListener {
            showUserAgreement()
        }

    }
}
