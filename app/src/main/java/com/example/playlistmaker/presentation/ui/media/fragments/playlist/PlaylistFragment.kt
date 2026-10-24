package com.example.playlistmaker.presentation.ui.media.fragments.playlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.presentation.viewmodelutils.ViewModelFactory
import javax.inject.Inject

class PlaylistFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as App).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: PlaylistViewModel by viewModels {
        viewModelFactory
    }
    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding ?: throw RuntimeException("FragmentPlaylistBinding is null!")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("MY_TAG", "$viewModel")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }
}