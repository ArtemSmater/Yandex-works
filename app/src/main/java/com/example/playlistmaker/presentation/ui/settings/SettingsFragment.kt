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
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.di.Creator
import com.example.playlistmaker.presentation.utils.FragmentTheme
import com.example.playlistmaker.presentation.utils.IntentFactory
import com.example.playlistmaker.presentation.utils.checkTheme
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class SettingsFragment : Fragment() {

    private val viewModelFactory by lazy {
        SettingsViewModelFactory(
            Creator.getThemeUseCase,
            requireActivity().application
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[SettingsViewModel::class.java]
    }
    private var disposable: Disposable? = null

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
        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = true))
        observeEffects()
        observeActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
        disposable = null
        _binding = null
    }

    private fun observeEffects() {
        viewModel.themeViewModel.observe(viewLifecycleOwner, Observer {
            binding.switchTheme.isChecked = it
        })

        disposable = viewModel.settingsViewModelEffects
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { checkEffects(it) }
    }

    private fun checkEffects(effect: SettingsUiEffects) {
        when (effect) {
            is SettingsUiEffects.BackPressed -> {
                doBackAction()
            }

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

            tbSettings.setNavigationOnClickListener {
                viewModel.uiAction(SettingsUiActions.BackPressed)
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

    private fun doBackAction() {
        findNavController().popBackStack()
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