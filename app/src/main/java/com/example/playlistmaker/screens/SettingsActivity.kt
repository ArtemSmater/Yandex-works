package com.example.playlistmaker.screens

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.IntentFactory
import com.example.playlistmaker.utils.PrefsUtil
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var switchTheme: SwitchMaterial
    private lateinit var tvShare: TextView
    private lateinit var tvSupport: TextView
    private lateinit var tvAgreement: TextView
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initViews()
        setDefaultCondition()
    }

    override fun onResume() {
        super.onResume()
        listeners()
    }

    private fun setDefaultCondition() {
        val isChecked = sharedPreferences.getBoolean(PrefsUtil.SHARED_KEY_THEME, false)
        if (isChecked) {
            switchTheme.isChecked = true
        }
    }

    private fun initViews() {
        toolbar = findViewById(R.id.tbSettings)
        switchTheme = findViewById(R.id.switchTheme)
        tvShare = findViewById(R.id.tvShare)
        tvSupport = findViewById(R.id.tvSupport)
        tvAgreement = findViewById(R.id.tvAgreement)
        sharedPreferences = getSharedPreferences(PrefsUtil.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    }

    fun listeners() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        switchTheme.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        tvShare.setOnClickListener {
            startActivity(IntentFactory.getShareIntent(this))
        }

        tvSupport.setOnClickListener {
            startActivity(IntentFactory.getSupportIntent(this))
        }

        tvAgreement.setOnClickListener {
            startActivity(IntentFactory.getAgreementIntent(this))
        }
    }
}