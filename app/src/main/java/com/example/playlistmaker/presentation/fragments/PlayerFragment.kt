package com.example.playlistmaker.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.PlayerFragmentBinding
import com.example.playlistmaker.presentation.utils.configureSystemBars
import com.example.playlistmaker.presentation.utils.isNightMode

class PlayerFragment : Fragment() {

    private var _binding: PlayerFragmentBinding? = null
    private val binding: PlayerFragmentBinding
        get() = _binding ?: throw RuntimeException("Player fragment binding is null!")

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
        val args = PlayerFragmentArgs.fromBundle(requireArguments())
        binding.track = args.Track
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        listeners()
        checkTheme()
    }

    private fun listeners() {
        with(binding) {
            tvAlbumValue.isSelected = true
            tbPlayer.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun checkTheme() {
        if (isNightMode()) {
            configureSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = false)
        } else {
            configureSystemBars(lightStatusBarIcons = true, lightNavigationBarIcons = true)
        }
    }
}