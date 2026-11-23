package com.example.playlistmaker.presentation.ui.media.children.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentFavouriteBinding
import com.example.playlistmaker.presentation.ui.media.MediaFragmentDirections
import com.example.playlistmaker.presentation.ui.search.SearchFragment.Companion.CLICK_DELAY_VALUE
import com.example.playlistmaker.presentation.ui.search.TrackAdapter
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class FavouriteFragment : Fragment() {

    private var isClickAllowed = true

    private val component by lazy {
        (requireActivity().application as App).appComponent
    }

    private val adapter by lazy {
        TrackAdapter {
            if (getClickPermission()) {
                viewModel.action(FavouriteUiActions.TrackClicked(it))
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: FavouriteViewModel by viewModels {
        viewModelFactory
    }
    private var _binding: FragmentFavouriteBinding? = null
    private val binding: FragmentFavouriteBinding
        get() = _binding ?: throw RuntimeException("FragmentFavouriteBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavourites.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        renderScreen(it)
                    }
                }

                launch {
                    viewModel.effect.collect {
                        renderEffect(it)
                    }
                }
            }
        }
    }

    private fun renderScreen(state: FavouriteUiState) {
        when (state) {
            is FavouriteUiState.Content -> {
                showError(false)
                adapter.submitList(state.tracks)
            }

            is FavouriteUiState.Empty -> {
                showError(true)
                adapter.submitList(emptyList())
            }
        }
    }

    private fun showError(isEmpty: Boolean) {
        with(binding) {
            ivFavouritePlaceholder.isVisible = isEmpty
            tvFavouriteMessage.isVisible = isEmpty
        }
    }

    private fun renderEffect(effect: FavouriteUiEffect) {
        when (effect) {
            is FavouriteUiEffect.OpenPlayer -> {
                findNavController().navigate(
                    MediaFragmentDirections.actionMediaFragmentToPlayerFragment(
                        effect.track
                    )
                )
            }
        }
    }

    private fun getClickPermission(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DELAY_VALUE.milliseconds)
                isClickAllowed = true
            }
        }
        return current
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): FavouriteFragment {
            return FavouriteFragment()
        }
    }
}