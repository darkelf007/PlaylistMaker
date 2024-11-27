package com.android.playlistmaker.main.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityMainBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener

class MainActivity : AppCompatActivity(), BottomNavigationListener {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
    private var isKeyboardVisible = false

    private val hiddenBottomNavFragments = setOf(
        R.id.playerFragment,
        R.id.newPlaylistFragment,
        R.id.editPlaylistFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setupWithNavController(navController)
        setupDestinationChangeListener()
        setupKeyboardVisibilityListener()
    }

    private fun setupDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            updateBottomNavigationVisibility()
        }
    }

    override fun toggleBottomNavigationViewVisibility(isVisible: Boolean) {
        binding.bottomNavigationView.isVisible = isVisible
    }

    private fun setupKeyboardVisibilityListener() {
        val contentView = findViewById<View>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - r.bottom
            val isKeyboardNowVisible = keypadHeight > screenHeight * 0.15
            if (isKeyboardNowVisible != isKeyboardVisible) {
                isKeyboardVisible = isKeyboardNowVisible
                updateBottomNavigationVisibility()
            }
        }
    }

    private fun updateBottomNavigationVisibility() {
        val shouldHideBottomNav =
            hiddenBottomNavFragments.contains(navController.currentDestination?.id)
        binding.bottomNavigationView.isVisible = !isKeyboardVisible && !shouldHideBottomNav
    }

}
