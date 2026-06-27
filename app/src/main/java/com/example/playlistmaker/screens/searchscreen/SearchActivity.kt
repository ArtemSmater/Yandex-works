package com.example.playlistmaker.screens.searchscreen

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.interfaces.ErrorSubscriber
import com.example.playlistmaker.R
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.pojo.Track

class SearchActivity : AppCompatActivity(), TrackSubscriber, ErrorSubscriber {
    private lateinit var toolbar: Toolbar
    private lateinit var ivClear: ImageView
    private lateinit var etSearch: EditText
    private lateinit var rvTracks: RecyclerView
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnToUpdate: Button
    private val adapter = TrackAdapter()
    private val presenter = TrackPresenter(this, this)
    private var searchValue = DEF_LINE
    private var searchCursor = DEF_CURSOR
    private var query = DEF_LINE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initViews()
        hideViews()

        if (savedInstanceState != null) {
            searchValue = savedInstanceState.getString(
                resources.getString(R.string.save_state_key),
                DEF_LINE
            )
            searchCursor = savedInstanceState.getInt(
                resources.getString(R.string.save_cursor_key),
                DEF_CURSOR
            )
            ivPlaceholder.visibility = savedInstanceState.getInt(
                resources.getString(R.string.save_error_img_key),
                DEF_VISIBILITY
            )
            tvErrorMessage.visibility = savedInstanceState.getInt(
                resources.getString(R.string.save_error_text_key),
                DEF_VISIBILITY
            )
            btnToUpdate.visibility = savedInstanceState.getInt(
                resources.getString(R.string.save_error_button_key),
                DEF_VISIBILITY
            )
            query = savedInstanceState.getString(
                resources.getString(R.string.save_query_key),
                DEF_LINE
            )
            if (savedInstanceState.getBoolean(resources.getString(R.string.save_list_size)) && query.isNotEmpty()) {
                presenter.loadTracks(query)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchValue.isNotEmpty()) {
            outState.putString(
                resources.getString(R.string.save_state_key),
                searchValue
            )
            outState.putInt(
                resources.getString(R.string.save_cursor_key),
                getCursorPosition()
            )
        }
        outState.putBoolean(
            resources.getString(R.string.save_list_size),
            adapter.tracks.isNotEmpty()
        )
        outState.putString(
            resources.getString(R.string.save_query_key),
            query
        )
        outState.putInt(
            resources.getString(R.string.save_error_img_key),
            ivPlaceholder.visibility
        )
        outState.putInt(
            resources.getString(R.string.save_error_text_key),
            tvErrorMessage.visibility
        )
        outState.putInt(
            resources.getString(R.string.save_error_button_key),
            btnToUpdate.visibility
        )
    }

    fun initViews() {
        toolbar = findViewById(R.id.tbSearch)
        ivClear = findViewById(R.id.ivClear)
        etSearch = findViewById(R.id.etSearch)
        ivPlaceholder = findViewById(R.id.ivErrorPlaceholder)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        btnToUpdate = findViewById(R.id.btnToUpload)

        rvTracks = findViewById(R.id.rvTracks)
        rvTracks.layoutManager = LinearLayoutManager(this)
        rvTracks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        listeners()
        setText(searchValue, searchCursor)
        checkViewState()
    }

    fun listeners() {
        toolbar.setNavigationOnClickListener { finish() }

        btnToUpdate.setOnClickListener { presenter.loadTracks(query) }

        ivClear.setOnClickListener { _ ->
            run {
                adapter.clear()
                setText(DEF_LINE, DEF_CURSOR)
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
        }

        etSearch.doOnTextChanged { text, _, _, _ ->
            searchValue = text.toString().trim()
            ivClear.isVisible = !text.isNullOrEmpty()
        }

        etSearch.doAfterTextChanged {
            it?.toString()?.let { text ->
                if (text.contains("  ") && text.length - text.replace(" ", "").length >= 2) {
                    val filtered = text.replace("  ", " ")
                    etSearch.setText(filtered)
                    etSearch.setSelection(filtered.length)
                }
            }
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                query = etSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    presenter.loadTracks(query)
                }
            }
            false
        }
    }

    fun setText(line: String, selection: Int) {
        etSearch.setText(line)
        etSearch.setSelection(selection)
    }

    override fun trackList(tracks: List<Track>) {
        showErrorHints(isEmpty = tracks.isEmpty(), isSuccess = true)
        adapter.tracks = tracks
    }

    override fun showError(message: String) {
        showErrorHints(isEmpty = true, isSuccess = false)
        adapter.clear()
    }

    fun showErrorHints(isEmpty: Boolean, isSuccess: Boolean) {
        if (isEmpty) {
            showViews()
            if (isSuccess) {
                btnToUpdate.visibility = View.GONE
            }
            checkViewState()
        } else hideViews()
    }

    fun showViews() {
        ivPlaceholder.visibility = View.VISIBLE
        tvErrorMessage.visibility = View.VISIBLE
        btnToUpdate.visibility = View.VISIBLE
    }

    fun hideViews() {
        ivPlaceholder.visibility = View.GONE
        tvErrorMessage.visibility = View.GONE
        btnToUpdate.visibility = View.GONE
    }

    fun checkViewState() {
        if (ivPlaceholder.isVisible && tvErrorMessage.isVisible && btnToUpdate.isVisible) {
            ivPlaceholder.setImageDrawable(theme.getDrawable(R.drawable.error_light))
            tvErrorMessage.text = buildString {
                append(getString(R.string.internet_error))
                append(getString(R.string.spaces))
                append(getString(R.string.download_error))
            }
            return
        }

        if (ivPlaceholder.isVisible && tvErrorMessage.isVisible) {
            ivPlaceholder.setImageDrawable(theme.getDrawable(R.drawable.empty_light))
            tvErrorMessage.text = getString(R.string.empty_list)
        }
    }

    fun getCursorPosition(): Int {
        val line = etSearch.text.toString()
        return if (line.endsWith(" ")) {
            line.length - 1
        } else {
            line.length
        }
    }
    companion object {
        const val DEF_LINE = ""
        const val DEF_CURSOR = 0
        const val DEF_VISIBILITY = View.GONE
    }
}