package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    companion object {
        const val DEF_LINE = ""
        const val DEF_CURSOR = 0
    }

    private lateinit var toolbar: Toolbar
    private lateinit var ivClear: ImageView
    private lateinit var etSearch: EditText
    private var searchValue = DEF_LINE
    private var searchCursor = DEF_CURSOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()

        if (savedInstanceState != null) {
            searchValue = savedInstanceState.getString(resources.getString(R.string.save_state_key), DEF_LINE)
            searchCursor = savedInstanceState.getInt(resources.getString(R.string.save_cursor_key), DEF_CURSOR)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (!searchValue.isEmpty()) {
            outState.putString(resources.getString(R.string.save_state_key), searchValue)
            outState.putInt(resources.getString(R.string.save_cursor_key), etSearch.selectionStart)
        }
    }

    fun initViews() {
        toolbar = findViewById(R.id.tbSearch)
        ivClear = findViewById(R.id.ivClear)
        etSearch = findViewById(R.id.etSearch)
    }

    override fun onResume() {
        listeners()
        setText(searchValue, searchCursor)
        super.onStart()
    }

    fun listeners() {
        ivClear.setOnClickListener { _ ->
            run {
                setText(DEF_LINE, DEF_CURSOR)
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
        }
        toolbar.setNavigationOnClickListener {
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                searchValue = s.toString().trim()
                hideClearButton()
            }
        })
    }

    fun hideClearButton() {
        if (etSearch.text.trim().isEmpty()) {
            ivClear.visibility = View.GONE
        } else {
            ivClear.visibility = View.VISIBLE
        }
    }

    fun setText(line: String, selection: Int) {
        etSearch.setText(line)
        etSearch.setSelection(selection)
    }
}