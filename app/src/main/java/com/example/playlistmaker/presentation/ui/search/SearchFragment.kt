package com.example.playlistmaker.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.utils.FragmentTheme
import com.example.playlistmaker.presentation.utils.Transform
import com.example.playlistmaker.presentation.utils.checkTheme
import com.example.playlistmaker.presentation.utils.hideKeyboard
import com.example.playlistmaker.presentation.utils.moveGuideline
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlin.math.max

class SearchFragment : Fragment() {

    // adapters
    private val trackAdapter by lazy {
        TrackAdapter()
    }

    private val cacheAdapter by lazy {
        TrackAdapter()
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this, SearchViewModel.getFactory(
                Creator.getTrackListUseCase,
                Creator.getHistoryListUseCase,
                Creator.addTrackToSearchHistoryUseCase,
                Creator.clearHistoryUseCase
            )
        )[SearchViewModel::class.java]
    }

    private val compositeDisposable = CompositeDisposable()

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
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime())
            val maxValue = max(
                Transform.dpToPx(bars.bottom.toFloat(), requireActivity()),
                Transform.dpToPx(210f, requireActivity()) - imeHeight.bottom
            )
            moveGuideline(maxValue, v)
            v.updatePadding(top = bars.top, bottom = max(bars.bottom, imeHeight.bottom))
            insets
        }
        setAdapters()
        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = true))
        observeActions()
        observeChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
        _binding = null
    }

    private fun observeChanges() {
        viewModel.searchViewModelState
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { checkScreenState(it) }
            .let(compositeDisposable::add)

        viewModel.searchViewModelEffect
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { checkScreenEffect(it) }
            .let(compositeDisposable::add)
    }

    private fun observeActions() {
        with(binding) {
            ivClear.isVisible = etSearch.text.isNotEmpty()
            etSearch.doOnTextChanged { text, _, _, _ ->
                ivClear.isVisible = !text.isNullOrEmpty()
                viewModel.uiAction(
                    SearchUiAction.FieldChanged(etSearch.isFocused, text)
                )
            }

            etSearch.onFocusChangeListener = View.OnFocusChangeListener { view, bool ->
                viewModel.uiAction(
                    SearchUiAction.FieldChanged(bool, (view as EditText).text.toString())
                )
            }

            btnToClearCache.setOnClickListener {
                viewModel.uiAction(SearchUiAction.ClearHistory)
            }

            btnToUpload.setOnClickListener {
                viewModel.uiAction(SearchUiAction.RetryQuery)
            }

            ivClear.setOnClickListener {
                viewModel.uiAction(SearchUiAction.FieldChanged(false, null))
            }

            trackAdapter.onTrackClickListener = {
                viewModel.uiAction(SearchUiAction.TrackClicked(it))
            }

            cacheAdapter.onTrackClickListener = {
                viewModel.uiAction(SearchUiAction.TrackClicked(it))
            }

            tbSearch.setNavigationOnClickListener {
                viewModel.uiAction(SearchUiAction.BackPressed)
            }
        }
    }

    private fun checkScreenEffect(state: SearchUiEffect) {
        when (state) {
            is SearchUiEffect.BackPressed -> {
                findNavController().popBackStack()
            }

            is SearchUiEffect.OpenPlayer -> {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToPlayerFragment(state.track)
                )
            }
        }
    }

    private fun checkScreenState(state: SearchUiState) {
        when (state) {
            is SearchUiState.Initial -> {
                showInitialScreen()
            }

            is SearchUiState.Loading -> {
                showLoadingScreen()
            }

            is SearchUiState.WebTracks -> {
                showTracksScreen(state.tracks)
            }

            is SearchUiState.HistoryTracks -> {
                showCacheScreen(state.tracks)
            }

            is SearchUiState.Error -> {
                showErrorScreen(state.error)
            }
        }
    }

    // main ui states methods
    private fun showInitialScreen() {
        binding.pbSearch?.isVisible = false
        defaultField(binding.etSearch)
        trackAdapter.submitList(emptyList())
        cacheVisibility(false)
        checkErrorState(SearchFragmentErrors.HideSearchErrors)
    }

    private fun showCacheScreen(tracks: List<Track>) {
        binding.pbSearch?.isVisible = false
        cacheAdapter.submitList(tracks)
        trackAdapter.submitList(emptyList())
        cacheVisibility(tracks.isNotEmpty())
        checkErrorState(SearchFragmentErrors.HideSearchErrors)
    }

    private fun showLoadingScreen() {
        binding.etSearch.hideKeyboard(requireActivity())
        binding.pbSearch?.isVisible = true
        trackAdapter.submitList(emptyList())
        cacheVisibility(false)
        checkErrorState(SearchFragmentErrors.HideSearchErrors)
    }

    private fun showTracksScreen(tracks: List<Track>) {
        binding.pbSearch?.isVisible = false
        trackAdapter.submitList(tracks)
        cacheVisibility(false)
        checkErrorState(SearchFragmentErrors.HideSearchErrors)
    }

    private fun showErrorScreen(state: SearchFragmentErrors) {
        binding.pbSearch?.isVisible = false
        trackAdapter.submitList(emptyList())
        cacheVisibility(false)
        when (state) {
            is SearchFragmentErrors.InternetConnection -> {
                checkErrorState(state)
            }

            is SearchFragmentErrors.HideSearchErrors -> {
                checkErrorState(state)
            }

            is SearchFragmentErrors.EmptyResponse -> {
                checkErrorState(state)
            }
        }
    }

    // visibility settings
    private fun cacheVisibility(visibility: Boolean) {
        with(binding) {
            tvCacheTitle.isVisible = visibility
            rvTracksCache.isVisible = visibility
            btnToClearCache.isVisible = visibility
        }
    }

    private fun checkErrorState(state: SearchFragmentErrors) {
        with(binding) {
            tvErrorMessage.isVisible = state.tvVisible
            ivErrorPlaceholder.isVisible = state.ivVisible
            btnToUpload.isVisible = state.bnVisible
            ivErrorPlaceholder.setImageDrawable(state.errorImg)
            tvErrorMessage.text = state.errorMsg
        }
    }

    private fun defaultField(editText: EditText) {
        editText.hideKeyboard(requireActivity())
        editText.setText("")
        editText.clearFocus()
    }

    private fun setAdapters() {
        binding.rvTracks.adapter = trackAdapter
        binding.rvTracksCache.adapter = cacheAdapter
    }
}