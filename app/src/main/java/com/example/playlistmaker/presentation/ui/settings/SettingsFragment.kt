package com.example.playlistmaker.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.App
import com.example.playlistmaker.data.localcache.CacheRepositoryProvider
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.presentation.utils.IntentFactory
import com.example.playlistmaker.presentation.utils.configureSystemBars
import com.example.playlistmaker.presentation.utils.isNightMode

class SettingsFragment : Fragment() {

    private val viewModelFactory by lazy {
        SettingsViewModelFactory(CacheRepositoryProvider.provideCacheRepository(requireActivity()))
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }

    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding
        get() = _binding ?: throw RuntimeException("Settings fragment binding is null!")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        observers()
        listeners()
    }

    override fun onResume() {
        super.onResume()
        checkTheme()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers() {
        viewModel.setTheme()
        viewModel.themeViewModel.observe(viewLifecycleOwner, Observer {
            binding.switchTheme.isChecked = it
        })
    }

    private fun listeners() {
        with(binding) {
            switchTheme.setOnCheckedChangeListener { _, checked ->
                viewModel.updateTheme(checked)
                (requireActivity().application as App).switchTheme(checked)
            }

            tbSettings.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            tvShare.setOnClickListener {
                startActivity(IntentFactory.getShareIntent(requireActivity()))
            }

            tvSupport.setOnClickListener {
                startActivity(IntentFactory.getSupportIntent(requireActivity()))
            }

            tvAgreement.setOnClickListener {
                startActivity(IntentFactory.getAgreementIntent(requireActivity()))
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