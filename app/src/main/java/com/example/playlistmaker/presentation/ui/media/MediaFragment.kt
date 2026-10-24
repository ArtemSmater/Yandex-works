package com.example.playlistmaker.presentation.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.MediaFragmentBinding
import com.example.playlistmaker.presentation.ui.media.fragments.InnerFragments
import com.example.playlistmaker.presentation.utils.FragmentTheme
import com.example.playlistmaker.presentation.utils.checkTheme
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private var _binding: MediaFragmentBinding? = null
    private val binding: MediaFragmentBinding
        get() = _binding ?: throw RuntimeException("Media fragment binding is null!")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MediaFragmentBinding.inflate(inflater, container, false)
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
        setViewPagerSettings()
        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = true))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewPagerSettings() {
        with(binding) {
            val adapter = MediaFragmentAdapter(this@MediaFragment)
            vpMedia.adapter = adapter
            TabLayoutMediator(tlMedia, vpMedia) { tab, position ->
                tab.text = getString(
                    when (InnerFragments.entries[position]) {
                        InnerFragments.PLAYLIST -> R.string.playlists_fragment
                        InnerFragments.FAVOURITE -> R.string.favourites_fragment
                    }
                )
            }.attach()

            tbMedia.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}