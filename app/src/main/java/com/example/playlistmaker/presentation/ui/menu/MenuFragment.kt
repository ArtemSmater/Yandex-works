package com.example.playlistmaker.presentation.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.MenuFragmentBinding
import com.example.playlistmaker.presentation.utils.configureSystemBars
import kotlinx.coroutines.launch


class MenuFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MenuViewModel::class.java]
    }

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
        configureSystemBars(lightStatusBarIcons = false, lightNavigationBarIcons = false)
        observeActions()
        observeChanges()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeActions() {
        with(binding) {
            btnSearch.setOnClickListener { viewModel.uiActions(MenuUiActions.LaunchSearchFragment) }
            btnMedia.setOnClickListener { viewModel.uiActions(MenuUiActions.LaunchMediaFragment) }
            btnSettings.setOnClickListener { viewModel.uiActions(MenuUiActions.LaunchSettingsFragment) }
        }
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.actions.collect { render(it) }
                }
            }
        }
    }


    private fun render(action: MenuUiActions) {
        when (action) {
            is MenuUiActions.LaunchSearchFragment -> {
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSearchFragment())
            }

            is MenuUiActions.LaunchMediaFragment -> {
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToMediaFragment())
            }

            is MenuUiActions.LaunchSettingsFragment -> {
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment())
            }
        }
    }
}