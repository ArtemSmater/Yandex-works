package com.example.playlistmaker.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.utils.FragmentTheme
import com.example.playlistmaker.presentation.utils.checkTheme

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            window.isNavigationBarContrastEnforced = false
            v.updatePadding(top = bars.top, bottom = bars.bottom)
            insets
        }
        checkTheme(FragmentTheme(lightSB = false, darkSB = true, lightNB = false, darkNB = true))
        setContentView(binding.root)
        setupNavigationBar()
    }

    private fun setupNavigationBar() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainFragmentContainer) as NavHostFragment

        val navController = navHostFragment
            .navController

        binding.mainBottomNavigation
            .setupWithNavController(navController)

        setupDestinationListener(navController)
        setToolbarNavigateListener(navController)
    }

    private fun setupDestinationListener(controller: NavController) {
        controller.addOnDestinationChangedListener { _, destination, _ ->
            with(binding) {
                val visibility = when (destination.id) {
                    R.id.searchFragment -> {
                        mainToolbar.setTitle(getString(R.string.search))
                        mainToolbar.setNavigationIcon(null)
                        View.VISIBLE
                    }

                    R.id.mediaFragment -> {
                        mainToolbar.setTitle(getString(R.string.media))
                        mainToolbar.setNavigationIcon(null)
                        View.VISIBLE
                    }

                    R.id.settingsFragment -> {
                        mainToolbar.setTitle(getString(R.string.settings))
                        mainToolbar.setNavigationIcon(null)
                        View.VISIBLE
                    }

                    else -> {
                        mainToolbar.setTitle("")
                        mainToolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
                        View.GONE
                    }
                }

                mainBottomNavigation.visibility = visibility
                mainDivider.visibility = visibility
            }
        }
    }

    private fun setToolbarNavigateListener(controller: NavController) {
        binding.mainToolbar.setNavigationOnClickListener {
            controller.popBackStack()
        }
    }
}