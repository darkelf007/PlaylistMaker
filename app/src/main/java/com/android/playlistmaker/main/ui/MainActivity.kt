package com.android.playlistmaker.main.ui

import android.graphics.Rect
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        constraintLayout = findViewById(R.id.main_constraint_layout)

        setupDestinationChangeListener()
        setupKeyboardVisibilityListener()
    }

    private fun setupDestinationChangeListener() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val shouldHideBottomNav = hiddenBottomNavFragments.contains(destination.id)

            if (shouldHideBottomNav) {
                hideBottomNavigation(true)
            } else {
                hideBottomNavigation(isKeyboardVisible)
            }
        }
    }

    override fun toggleBottomNavigationViewVisibility(isVisible: Boolean) {
        binding.bottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
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
                if (bottomNavigationView.visibility != View.GONE) {
                    bottomNavigationView.visibility = View.GONE
                }
                return@addOnGlobalLayoutListener
            }

            if (isKeyboardNowVisible != isKeyboardVisible) {
                isKeyboardVisible = isKeyboardNowVisible
                if (isKeyboardVisible) {
                    hideBottomNavigation(true)

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(
                        R.id.nav_host_fragment,
                        ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.BOTTOM
                    )
                    TransitionManager.beginDelayedTransition(constraintLayout)
                    constraintSet.applyTo(constraintLayout)
                } else {
                    hideBottomNavigation(false)

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(
                        R.id.nav_host_fragment,
                        ConstraintSet.BOTTOM,
                        R.id.bottomNavigationView,
                        ConstraintSet.TOP
                    )
                    TransitionManager.beginDelayedTransition(constraintLayout)
                    constraintSet.applyTo(constraintLayout)
                }
            }
        }
    }

    private fun hideBottomNavigation(isHide: Boolean) {
        bottomNavigationView.visibility = if (!isHide) View.VISIBLE else View.GONE
    }
}
