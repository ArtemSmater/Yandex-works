package com.example.playlistmaker.presentation.ui.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchFragmentBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentation.utils.hideKeyboard
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).appComponent
    }

    private val trackAdapter by lazy {
        TrackAdapter()
    }

    private val cacheAdapter by lazy {
        TrackAdapter()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SearchViewModel by viewModels {
        viewModelFactory
    }

    // view binding
    private var _binding: SearchFragmentBinding? = null
    private val binding: SearchFragmentBinding
        get() = _binding ?: throw RuntimeException("Search fragment binding is null!")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

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
        setAdapters()
        observeActions()
        observeChanges()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        checkScreenState(it)
                    }
                }

                launch {
                    viewModel.effect.collect {
                        checkScreenEffect(it)
                    }
                }
            }
        }
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
        }
    }

    private fun checkScreenEffect(state: SearchUiEffect) {
        when (state) {
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
                setErrorsRes(false)
            }

            is SearchFragmentErrors.HideSearchErrors -> {
                checkErrorState(state)
            }

            is SearchFragmentErrors.EmptyResponse -> {
                checkErrorState(state)
                setErrorsRes(true)
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

    private fun setErrorsRes(isEmpty: Boolean) {
        with(binding) {
            tvErrorMessage.text = getErrorMessage(isEmpty)
            ivErrorPlaceholder.setImageDrawable(getErrorDrawable(isEmpty))
        }
    }


    private fun checkErrorState(state: SearchFragmentErrors) {
        with(binding) {
            tvErrorMessage.isVisible = state.tvVisible
            ivErrorPlaceholder.isVisible = state.ivVisible
            btnToUpload.isVisible = state.bnVisible
        }
    }

    private fun getErrorDrawable(isEmpty: Boolean): Drawable {
        return if (isEmpty) {
            requireActivity().theme.getDrawable(R.drawable.empty_light)
        } else {
            requireActivity().theme.getDrawable(R.drawable.error_light)
        }
    }

    private fun getErrorMessage(isEmpty: Boolean): StringBuilder {
        return if (isEmpty) {
            StringBuilder(requireActivity().getString(R.string.empty_list))
        } else {
            StringBuilder(requireActivity().getString(R.string.internet_error))
                .append(requireActivity().getString(R.string.spaces))
                .append(requireActivity().getString(R.string.download_error))
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