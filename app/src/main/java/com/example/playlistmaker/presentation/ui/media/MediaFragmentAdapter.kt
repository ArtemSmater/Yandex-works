package com.example.playlistmaker.presentation.ui.media

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.ui.media.fragments.InnerFragments
import com.example.playlistmaker.presentation.ui.media.fragments.favourite.FavouriteFragment
import com.example.playlistmaker.presentation.ui.media.fragments.playlist.PlaylistFragment

class MediaFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteFragment.newInstance()
            1 -> PlaylistFragment.newInstance()
            else -> throw RuntimeException("Unable to create unknown fragment!")
        }
    }

    override fun getItemCount(): Int {
        return InnerFragments.entries.size
    }
}