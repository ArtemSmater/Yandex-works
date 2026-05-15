package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.statusBars())
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
    }
}