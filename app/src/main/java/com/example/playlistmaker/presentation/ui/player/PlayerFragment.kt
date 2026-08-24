package com.example.playlistmaker.presentation.ui.player

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayerFragmentBinding
import com.example.playlistmaker.presentation.utils.configureSystemBars
import com.example.playlistmaker.presentation.utils.isNightMode
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class PlayerFragment : Fragment() {

    private val viewModelFactory by lazy {
        val args = PlayerFragmentArgs.fromBundle(requireArguments())
        PlayerViewModelFactory(args.Track)
    }

    private lateinit var viewModel: PlayerViewModel

    private var effectDisposable: Disposable? = null

    private var _binding: PlayerFragmentBinding? = null
    private val binding: PlayerFragmentBinding
        get() = _binding ?: throw RuntimeException("Player fragment binding is null!")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[PlayerViewModel::class.java]
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
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        binding.track = PlayerFragmentArgs.fromBundle(requireArguments()).Track
        binding.lifecycleOwner = viewLifecycleOwner
        checkTheme()
    }

    override fun onResume() {
        super.onResume()
        observables()
        observers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.uiAction(PlayerUiAction.Pause)
    }

    override fun onStop() {
        super.onStop()
        effectDisposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.uiAction(PlayerUiAction.Release)
    }

    private fun observables() {
        with(binding) {
            tbPlayer.setNavigationOnClickListener {
                viewModel.uiAction(PlayerUiAction.Back)
            }

            ibStartSong.setOnClickListener {
                viewModel.uiAction(PlayerUiAction.Play)
            }
        }
    }

    private fun observers() {
        viewModel.playerViewModelState.observe(viewLifecycleOwner) {
            checkScreenState(it)
        }

        effectDisposable = viewModel.playerViewModelEffect
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { findNavController().popBackStack() }
    }

    private fun checkScreenState(state: PlayerUiState) {
        when (state) {
            is PlayerUiState.Initial -> {
                initialScreen()
            }

            is PlayerUiState.Prepared -> {
                preparedScreen(state.progress)
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

    private fun preparedScreen(progress: String) {
        with(binding) {
            ibStartSong.isEnabled = true
            ibStartSong.setImageDrawable(getPlayDrawable(false))
            tvSongDuration.text = progress
        }
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

    private fun checkTheme() {
        if (isNightMode()) {
            configureSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = false)
        } else {
            configureSystemBars(lightStatusBarIcons = true, lightNavigationBarIcons = true)
        }
    }
}