package com.example.playlistmaker.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.screens.searchscreen.SearchActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchButton = findViewById<Button>(R.id.btnSearch)
        val mediaButton = findViewById<Button>(R.id.btnMedia)
        val settingsButton = findViewById<Button>(R.id.btnSettings)

        searchButton.setOnClickListener(this)
        mediaButton.setOnClickListener(listener1)
        settingsButton.setOnClickListener(listener2)
    }

    // var 1
    override fun onClick(v: View?) {
        checkButton(v as Button)
    }

    // var 2
    val listener1: View.OnClickListener = View.OnClickListener { v -> checkButton(v as Button) }

    // var 3
    val listener2: View.OnClickListener = View.OnClickListener { v -> checkButton(v as Button) }

    fun checkButton(btn: Button) {
        val intent = when (btn.id) {
            R.id.btnSearch -> Intent(this, SearchActivity::class.java)
            R.id.btnMedia -> Intent(this, MediaActivity::class.java)
            R.id.btnSettings -> Intent(this, SettingsActivity::class.java)
            else -> throw RuntimeException(getString(R.string.unknown_button_error))
        }
        startActivity(intent)
    }
}