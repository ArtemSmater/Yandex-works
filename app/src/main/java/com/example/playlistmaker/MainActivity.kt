package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.windowInsetsController!!.hide(android.view.WindowInsets.Type.statusBars())

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
    val listener2: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            checkButton(v as Button)
        }
    }

    fun checkButton(btn: Button) {
        val intent = when (btn.id) {
            R.id.btnSearch -> Intent(this, SearchActivity::class.java)
            R.id.btnMedia -> Intent(this, MediaActivity::class.java)
            R.id.btnSettings -> Intent(this, SettingsActivity::class.java)
            else -> Intent(this, MediaActivity::class.java)
        }
        startActivity(intent)
    }
}