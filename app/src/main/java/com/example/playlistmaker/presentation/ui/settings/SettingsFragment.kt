package com.example.playlistmaker.presentation.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.presentation.utils.IntentFactory
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SettingsViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: SettingsFragmentBinding? = null
    private val binding: SettingsFragmentBinding
        get() = _binding ?: throw RuntimeException("Settings fragment binding is null!")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

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
        observeEffects()
        observeActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.themeViewModel.collect { binding.switchTheme.isChecked = it }
                }

                launch {
                    viewModel.settingsViewModelEffects.collect { checkEffects(it) }
                }
            }
        }
    }

    private fun checkEffects(effect: SettingsUiEffects) {
        when (effect) {
            is SettingsUiEffects.IntentShare -> {
                doShareIntent()
            }

            is SettingsUiEffects.IntentAgreement -> {
                doAgreementAction()
            }

            is SettingsUiEffects.IntentSupport -> {
                doSupportAction()
            }
        }
    }

    private fun observeActions() {
        with(binding) {
            switchTheme.setOnCheckedChangeListener { _, checked ->
                viewModel.uiAction(SettingsUiActions.UpdateTheme(checked))
            }

            tvShare.setOnClickListener {
                viewModel.uiAction(SettingsUiActions.ShareAction)
            }

            tvSupport.setOnClickListener {
                viewModel.uiAction(SettingsUiActions.SupportAction)
            }

            tvAgreement.setOnClickListener {
                viewModel.uiAction(SettingsUiActions.CheckAgreementAction)
            }
        }
    }

    private fun doShareIntent() {
        startActivity(IntentFactory.getShareIntent(requireActivity()))
    }

    private fun doSupportAction() {
        startActivity(IntentFactory.getSupportIntent(requireActivity()))
    }

    private fun doAgreementAction() {
        startActivity(IntentFactory.getAgreementIntent(requireActivity()))
    }
}