package com.example.playlistmaker.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.playlistmaker.R

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
            finish()
        }

        val tvShare = findViewById<TextView>(R.id.tvShare)
        tvShare.setOnClickListener { _ ->
            run {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.yandex_android_link))
                    type = resources.getString(R.string.intent_text_type)
                }
                startActivity(Intent.createChooser(intent, resources.getString(R.string.share_with)))
            }
        }

        val tvSupport = findViewById<TextView>(R.id.tvSupport)
        tvSupport.setOnClickListener { _ ->
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

        val tvAgreement = findViewById<TextView>(R.id.tvAgreement)
        tvAgreement.setOnClickListener { _ ->
            run {
                val intent = Intent(Intent.ACTION_VIEW, resources.getString(R.string.yandex_agreement_link).toUri())
                startActivity(intent)
            }
        }
    }
}