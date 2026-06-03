package com.example.playlistmaker
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible

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
        super.onResume()
        listeners()
        setText(searchValue, searchCursor)
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
            finish()
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
                ivClear.isVisible = !s.isNullOrEmpty()
            }
        })
    }

    fun setText(line: String, selection: Int) {
        etSearch.setText(line)
        etSearch.setSelection(selection)
    }
}