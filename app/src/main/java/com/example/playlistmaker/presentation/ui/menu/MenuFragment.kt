package com.example.playlistmaker.presentation.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.MenuFragmentBinding
import com.example.playlistmaker.presentation.utils.configureSystemBars

class MenuFragment : Fragment() {

    private var _binding: MenuFragmentBinding? = null
    private val binding: MenuFragmentBinding
        get() = _binding ?: throw RuntimeException("Menu fragment binding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            requireActivity().window.isNavigationBarContrastEnforced = false
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        listeners()
    }

    override fun onResume() {
        super.onResume()
        configureSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listeners() {
        binding.btnSearch.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSearchFragment())
        }

        binding.btnMedia.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToMediaFragment())
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment())
        }
    }
}