package com.android.playlistmaker.main.ui

import android.graphics.Rect
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.playlistmaker.R
import com.android.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var constraintLayout: ConstraintLayout
    private var isKeyboardVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navMainFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navMainFragment.navController

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        constraintLayout = findViewById(R.id.main_constraint_layout)

        setupKeyboardVisibilityListener()
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
                if (isKeyboardVisible) {
                    bottomNavigationView.visibility = View.GONE

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
                    bottomNavigationView.visibility = View.VISIBLE

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
}
