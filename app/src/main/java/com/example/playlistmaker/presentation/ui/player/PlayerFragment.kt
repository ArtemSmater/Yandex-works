package com.example.playlistmaker.presentation.ui.player

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerFragmentBinding
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).appComponent
    }

    private val args: PlayerFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: PlayerViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: PlayerFragmentBinding? = null
    private val binding: PlayerFragmentBinding
        get() = _binding ?: throw RuntimeException("Player fragment binding is null!")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlayerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        binding.track = args.track
        binding.lifecycleOwner = viewLifecycleOwner
        observeActions()
        observeChanges()
    }

    override fun onPause() {
        super.onPause()
        viewModel.uiAction(PlayerUiAction.Pause)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeActions() {
        with(binding) {
            ibStartSong.setOnClickListener {
                viewModel.uiAction(PlayerUiAction.Play)
            }
        }
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect {
                        checkScreenState(it)
                    }
                }
            }
        }
    }

    private fun checkScreenState(state: PlayerUiState) {
        when (state) {
            is PlayerUiState.Initial -> {
                initialScreen()
            }

            is PlayerUiState.Prepared -> {
                preparedScreen()
            }

            is PlayerUiState.Playing -> {
                playingScreen(state.progress)
            }

            is PlayerUiState.Paused -> {
                pausedScreen(state.progress)
            }
        }
    }

    private fun initialScreen() {
        binding.ibStartSong.isEnabled = false
    }

    private fun preparedScreen() {
        getActionScreen(requireActivity().getString(R.string.time_example), false)
    }

    private fun playingScreen(progress: String) {
        getActionScreen(progress, isPlaying = true)
    }

    private fun pausedScreen(progress: String) {
        getActionScreen(progress, isPlaying = false)
    }

    private fun getActionScreen(progress: String, isPlaying: Boolean) {
        with(binding) {
            ibStartSong.isEnabled = true
            ibStartSong.setImageDrawable(getPlayDrawable(isPlaying))
            tvSongDuration.text = progress
        }
    }

    private fun getPlayDrawable(isPlaying: Boolean): Drawable? {
        val drawableId = if (isPlaying) {
            R.drawable.pause_button
        } else {
            R.drawable.play_button
        }
        return AppCompatResources.getDrawable(requireActivity(), drawableId)
    }
}