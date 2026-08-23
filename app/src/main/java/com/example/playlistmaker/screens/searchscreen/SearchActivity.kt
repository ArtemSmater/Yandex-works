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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.interfaces.ErrorSubscriber
import com.example.playlistmaker.R
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.interfaces.CacheSubscriber
import com.example.playlistmaker.interfaces.TrackSubscriber
import com.example.playlistmaker.pojo.Track
import com.example.playlistmaker.utils.PrefsUtil
import com.example.playlistmaker.utils.Transform

class SearchActivity : AppCompatActivity(), TrackSubscriber, CacheSubscriber, ErrorSubscriber {

    // main views
    private lateinit var toolbar: Toolbar
    private lateinit var ivClear: ImageView
    private lateinit var etSearch: EditText
    private lateinit var rvTracks: RecyclerView

    // error views
    private lateinit var ivPlaceholder: ImageView
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnToUpdate: Button

    // cache list views
    private lateinit var tvCacheTitle: TextView
    private lateinit var rvTrackCache: RecyclerView
    private lateinit var btnToClearCache: Button

    // adapters
    private val trackAdapter = TrackAdapter()
    private val cacheAdapter = TrackAdapter()

    // info sources
    private val presenter = TrackPresenter(this, this)
    private lateinit var cache: SearchHistory

    // class fields
    private var searchValue = DEF_LINE
    private var searchCursor = DEF_CURSOR
    private var query = DEF_LINE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        getSavedStates(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        getCachedInfo()
    }

    override fun onResume() {
        super.onResume()
        listeners()
        setText(searchValue, searchCursor)
        checkViewState()
    }

    override fun onStop() {
        super.onStop()
        cache.fillCache(cacheAdapter.currentList)
    }

    private fun getCachedInfo() {
        val sharedPreferences = getSharedPreferences(PrefsUtil.SHARED_PREFERENCE_NAME, MODE_PRIVATE)
        cache = SearchHistory(sharedPreferences, this)
    }

    private fun initViews() {

        // main views
        toolbar = findViewById(R.id.tbSearch)
        ivClear = findViewById(R.id.ivClear)
        etSearch = findViewById(R.id.etSearch)

        // error views
        ivPlaceholder = findViewById(R.id.ivErrorPlaceholder)
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        btnToUpdate = findViewById(R.id.btnToUpload)

        // cache views
        tvCacheTitle = findViewById(R.id.tvCacheTitle)
        btnToClearCache = findViewById(R.id.btnToClearCache)

        // track recycler view
        rvTracks = findViewById(R.id.rvTracks)
        rvTracks.adapter = trackAdapter

        // cache recycler view
        rvTrackCache = findViewById(R.id.rvTrackCache)
        rvTrackCache.adapter = cacheAdapter
    }

    private fun getSavedStates(savedInstanceState: Bundle?) {
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
            if (savedInstanceState.getBoolean(resources.getString(R.string.save_list_size_key)) && query.isNotEmpty()) {
                presenter.loadTracks(query)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            resources.getString(R.string.save_state_key),
            searchValue
        )
        outState.putInt(
            resources.getString(R.string.save_cursor_key),
            getCursorPosition()
        )
        outState.putBoolean(
            resources.getString(R.string.save_list_size_key),
            trackAdapter.currentList.isNotEmpty()
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

    fun listeners() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnToUpdate.setOnClickListener {
            presenter.loadTracks(query)
        }

        btnToClearCache.setOnClickListener {
            cacheAdapter.submitList(emptyList<Track>())
            cacheViewsVisibility(View.GONE)
        }

        ivClear.setOnClickListener {
            trackAdapter.submitList(emptyList<Track>())
            setText(DEF_LINE, DEF_CURSOR)
            etSearch.clearFocus()
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }

        etSearch.doOnTextChanged { text, _, _, _ ->
            searchValue = text.toString().trim()
            ivClear.isVisible = !text.isNullOrEmpty()
            if (text.isNullOrEmpty()) {
                trackAdapter.submitList(emptyList<Track>())
            }
            showCacheList(
                etSearch.hasFocus(),
                text?.isEmpty() == true,
                text?.isEmpty() == true,
                cacheAdapter.currentList.isNotEmpty()
            )
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
                if (etSearch.text.isNotBlank()) {
                    query = etSearch.text.toString()
                    presenter.loadTracks(query)
                }
            }
            etSearch.clearFocus()
            false
        }

        etSearch.onFocusChangeListener = View.OnFocusChangeListener { _, focus ->
            showCacheList(
                focus,
                etSearch.text.isEmpty(),
                trackAdapter.currentList.isEmpty(),
                cacheAdapter.currentList.isNotEmpty()
            )
        }

        trackAdapter.onTrackClickListener = {
            val value = cacheAdapter.currentList.toMutableList()
            cacheAdapter.submitList(Transform.editList(it, value))
            rvTrackCache.scrollToPosition(0)
        }
    }

    fun setText(line: String, selection: Int) {
        etSearch.setText(line)
        etSearch.setSelection(selection)
    }

    override fun trackList(tracks: List<Track>) {
        showErrorHints(isEmpty = tracks.isEmpty(), isSuccess = true)
        trackAdapter.submitList(tracks)
    }

    override fun showError(message: String) {
        showErrorHints(isEmpty = true, isSuccess = false)
        trackAdapter.submitList(emptyList<Track>())
    }

    private fun showErrorHints(isEmpty: Boolean, isSuccess: Boolean) {
        errorViewsVisibility(if (isEmpty) View.VISIBLE else View.GONE)
        if (isSuccess) btnToUpdate.visibility = View.GONE
        checkViewState()
    }

    private fun showCacheList(
        hasFocus: Boolean,
        emptyField: Boolean,
        emptyList: Boolean,
        emptyCache: Boolean
    ) {
        cacheViewsVisibility(if (hasFocus && emptyField && emptyList && emptyCache) View.VISIBLE else View.GONE)
    }

    private fun errorViewsVisibility(visibility: Int) {
        ivPlaceholder.visibility = visibility
        tvErrorMessage.visibility = visibility
        btnToUpdate.visibility = visibility
    }

    private fun cacheViewsVisibility(visibility: Int) {
        if (visibility == View.VISIBLE) errorViewsVisibility(View.GONE)
        tvCacheTitle.visibility = visibility
        rvTrackCache.visibility = visibility
        btnToClearCache.visibility = visibility
    }

    private fun checkViewState() {
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

    private fun getCursorPosition(): Int {
        val line = etSearch.text.toString()
        return if (line.endsWith(" ")) {
            line.length - 1
        } else {
            line.length
        }
    }

    override fun getCacheTracks(list: MutableList<Track>) {
        cacheAdapter.submitList(list)
    }

    companion object {
        const val DEF_LINE = ""
        const val DEF_CURSOR = 0
        const val DEF_VISIBILITY = View.GONE
    }
}