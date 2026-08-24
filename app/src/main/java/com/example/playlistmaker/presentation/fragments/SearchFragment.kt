package com.example.playlistmaker.presentation.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.localcache.CacheRepositoryProvider
import com.example.playlistmaker.data.network.WebRepositoryProvider
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.adapters.TrackAdapter
import com.example.playlistmaker.presentation.utils.Transform
import com.example.playlistmaker.presentation.utils.configureSystemBars
import com.example.playlistmaker.presentation.utils.isNightMode
import com.example.playlistmaker.presentation.viewmodel.searchviewmodel.SearchViewModel
import com.example.playlistmaker.presentation.viewmodel.searchviewmodel.SearchViewModelFactory
import kotlin.math.max

class SearchFragment : Fragment() {

    // adapters
    private val trackAdapter by lazy {
        TrackAdapter()
    }

    private val cacheAdapter by lazy {
        TrackAdapter()
    }

    // class fields
    private var query = DEF_STRING_VALUE

    // view models for screen info
    private val viewModelFactory by lazy {
        SearchViewModelFactory(
            CacheRepositoryProvider.provideCacheRepository(requireActivity().application),
            WebRepositoryProvider.provideWebRepository()
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
    }

    // view binding
    private var _binding: SearchFragmentBinding? = null
    private val binding: SearchFragmentBinding
        get() = _binding ?: throw RuntimeException("Search fragment binding is null!")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLastQuery(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime())
            moveGuideline(
                max(
                    Transform.dpToPx(bars.bottom.toFloat(), requireActivity()),
                    Transform.dpToPx(210f, requireActivity()) - imeHeight.bottom
                ),
                v
            )
            v.updatePadding(top = bars.top, bottom = max(bars.bottom, imeHeight.bottom))
            insets
        }
        setAdapters()
        listeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getCacheList()
    }

    override fun onResume() {
        super.onResume()
        checkTheme()
        observers()
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCache(cacheAdapter.currentList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (query.isNotEmpty()) {
            outState.putString(KEY_LAST_QUERY, query)
        }
    }

    private fun moveGuideline(offset: Int, view: View) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(view as ConstraintLayout)
        constraintSet.setGuidelineBegin(
            R.id.glSearch,
            offset
        )
        TransitionManager.beginDelayedTransition(view)
        constraintSet.applyTo(view)
    }

    private fun checkLastQuery(savedState: Bundle?) {
        if (savedState != null) {
            query = savedState.getString(KEY_LAST_QUERY, DEF_STRING_VALUE)
        }
    }

    private fun checkTheme() {
        if (isNightMode()) {
            configureSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = false)
        } else {
            configureSystemBars(lightStatusBarIcons = true, lightNavigationBarIcons = true)
        }
    }

    private fun setAdapters() {
        binding.rvTracks.adapter = trackAdapter
        binding.rvTracksCache.adapter = cacheAdapter
    }

    private fun observers() {
        viewModel.trackListViewModel.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                selectTrackScreenMode()
            }
            trackAdapter.submitList(it)
        })

        viewModel.cacheListViewModel.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                selectTrackScreenMode()
            }
            cacheAdapter.submitList(it)
        })

        viewModel.emptyResponseViewModel.observe(viewLifecycleOwner, Observer {
            if (it) {
                selectErrorScreenMode()
                emptyListError()
            }
        })

        viewModel.webErrorViewModel.observe(viewLifecycleOwner, Observer {
            if (it) {
                selectErrorScreenMode()
                serverError()
            }
        })
    }


    private fun listeners() {
        with(binding) {
            tbSearch.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            btnToUpload.setOnClickListener {
                viewModel.getTrackList(query)
            }

            etSearch.onFocusChangeListener = View.OnFocusChangeListener { _, f ->
                checkCacheConditions(f)
            }

            ivClear.setOnClickListener {
                viewModel.getEmptyList()
                getDefaultField()
            }

            btnToClearCache.setOnClickListener {
                viewModel.getEmptyCache()
                cacheViewsVisibility(View.GONE)
            }

            etSearch.doOnTextChanged { text, _, _, _ ->
                ivClear.isVisible = !text.isNullOrEmpty()
                checkCacheConditions(etSearch.hasFocus())
            }

            trackAdapter.onTrackClickListener = {
                correctCurrentList(it)
                launchPlayerFragment(it)
            }

            cacheAdapter.onTrackClickListener = {
                launchPlayerFragment(it)
            }

            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (etSearch.text.isNotBlank()) {
                        query = etSearch.text.toString()
                        viewModel.getTrackList(query)
                        hideKeyboard(etSearch)
                    }
                }
                false
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
        }
    }

    private fun correctCurrentList(track: Track) {
        val value = cacheAdapter.currentList.toMutableList()
        cacheAdapter.submitList(Transform.editList(track, value))
        binding.rvTracksCache.scrollToPosition(0)
    }

    private fun launchPlayerFragment(track: Track) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        )
    }

    private fun getDefaultField() {
        with(binding.etSearch) {
            setText(DEF_STRING_VALUE)
            hideKeyboard(this)
        }
    }

    // hide keyboard method
    private fun hideKeyboard(editText: EditText) {
        editText.clearFocus()
        val inputMethodManager = requireActivity()
            .getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager
            ?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // conditions of cache visibility
    private fun checkCacheConditions(isFocused: Boolean) {
        val isNotEmpty = cacheAdapter.currentList.isNotEmpty()
        val isNotFilled = binding.etSearch.text.isEmpty()
        if (isFocused && isNotEmpty && isNotFilled) {
            cacheViewsVisibility(View.VISIBLE)
            if (binding.tvErrorMessage.isVisible) {
                selectTrackScreenMode()
            } else {
                selectErrorScreenMode()
            }
        } else {
            cacheViewsVisibility(View.GONE)
        }
    }

    // some types of mistakes
    private fun emptyListError() {
        errorViewsVisibility(View.VISIBLE)
        with(binding) {
            btnToUpload.visibility = View.GONE
            ivErrorPlaceholder.setImageDrawable(
                requireActivity().theme.getDrawable(R.drawable.empty_light)
            )
            tvErrorMessage.text = getString(R.string.empty_list)
        }
    }

    private fun serverError() {
        errorViewsVisibility(View.VISIBLE)
        with(binding) {
            ivErrorPlaceholder.setImageDrawable(
                requireActivity().theme.getDrawable(R.drawable.error_light)
            )
            tvErrorMessage.text = buildString {
                append(getString(R.string.internet_error))
                append(getString(R.string.spaces))
                append(getString(R.string.download_error))
            }
        }
    }

    // set screen mode
    private fun selectTrackScreenMode() {
        errorViewsVisibility(View.GONE)
    }

    private fun selectErrorScreenMode() {
        viewModel.getEmptyList()
    }

    // set visibilities
    private fun errorViewsVisibility(visibility: Int) {
        with(binding) {
            ivErrorPlaceholder.visibility = visibility
            tvErrorMessage.visibility = visibility
            btnToUpload.visibility = visibility
        }
    }

    private fun cacheViewsVisibility(visibility: Int) {
        with(binding) {
            tvCacheTitle.visibility = visibility
            rvTracksCache.visibility = visibility
            btnToClearCache.visibility = visibility
        }
    }

    companion object {
        private const val KEY_LAST_QUERY = "last_query"
        private const val DEF_STRING_VALUE = ""
    }
}