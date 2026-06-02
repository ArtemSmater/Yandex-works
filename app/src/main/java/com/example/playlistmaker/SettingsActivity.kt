package com.example.playlistmaker

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onStart() {
        listeners()
        super.onStart()
    }

    fun listeners() {
        val toolbar = findViewById<Toolbar>(R.id.tbSettings)
        toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        val shareIcon = findViewById<ImageView>(R.id.shareIcon)
        shareIcon.setOnClickListener { _ ->
            run {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.yandex_android_link))
                    type = resources.getString(R.string.intent_text_type)
                }
                startActivity(Intent.createChooser(intent, resources.getString(R.string.share_with)))
            }
        }

        val supportIcon = findViewById<ImageView>(R.id.supportIcon)
        supportIcon.setOnClickListener { _ ->
            run {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = resources.getString(R.string.intent_email_type).toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(resources.getString(R.string.yandex_android_link)))
                    putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.letter_topic))
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.letter_text))
                }
                startActivity(intent)
            }
        }

        val agreementIcon = findViewById<ImageView>(R.id.agreementIcon)
        agreementIcon.setOnClickListener { _ ->
            run {
                val intent = Intent(Intent.ACTION_VIEW, resources.getString(R.string.yandex_agreement_link).toUri())
                startActivity(intent)
            }
        }
    }
}