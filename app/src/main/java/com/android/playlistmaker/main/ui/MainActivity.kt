package com.android.playlistmaker.main.ui

import android.graphics.Rect
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityMainBinding
import com.android.playlistmaker.main.listeners.BottomNavigationListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var constraintLayout: ConstraintLayout
    private var isKeyboardVisible = false

    private val hiddenBottomNavFragments = setOf(
        R.id.playerFragment, R.id.newPlaylistFragment
    )

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navMainFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navMainFragment.navController

        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        constraintLayout = binding.mainConstraintLayout

        setupDestinationChangeListener()
        setupKeyboardVisibilityListener()
    }

    private fun setupDestinationChangeListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val shouldHideBottomNav = hiddenBottomNavFragments.contains(destination.id)
            binding.bottomNavigationView.isVisible = !shouldHideBottomNav && !isKeyboardVisible
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

            val currentDestinationId = navController.currentDestination?.id
            val shouldHideBottomNav = hiddenBottomNavFragments.contains(currentDestinationId)

            if (shouldHideBottomNav) {
                if (binding.bottomNavigationView.isVisible) {
                    binding.bottomNavigationView.isVisible = false
                }
                return@addOnGlobalLayoutListener
            }

            if (isKeyboardNowVisible != isKeyboardVisible) {
                isKeyboardVisible = isKeyboardNowVisible
                binding.bottomNavigationView.isVisible = !isKeyboardVisible && !shouldHideBottomNav
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                if (isKeyboardVisible) {
                    constraintSet.connect(
                        R.id.nav_host_fragment,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM
                    )
                } else {
                    constraintSet.connect(
                        R.id.nav_host_fragment,
                        ConstraintSet.BOTTOM,
                        R.id.bottomNavigationView,
                        ConstraintSet.TOP
                    )
                }
                TransitionManager.beginDelayedTransition(constraintLayout)
                constraintSet.applyTo(constraintLayout)
            }
        }
    }
}