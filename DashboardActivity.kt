package com.rama77.app

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rama77.app.databinding.ActivityDashboardBinding
import com.rama77.app.ui.funds.FundsFragment
import com.rama77.app.ui.history.HistoryFragment
import com.rama77.app.ui.home.HomeFragment
import com.rama77.app.ui.passbook.PassbookFragment
import com.rama77.app.ui.support.SupportFragment

/**
 * DashboardActivity provides direct Bottom Navigation between Home, History, Passbook, Funds, and Support
 */
class DashboardActivity : AppCompatActivity() {

    private var _binding: ActivityDashboardBinding? = null
    private val binding get() = _binding
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            _binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding?.root)
        } catch (e: Throwable) {
            setContentView(R.layout.activity_dashboard)
        }

        try {
            setupBottomNavigation()
        } catch (e: Throwable) {
            // Ignore securely
        }

        if (savedInstanceState == null) {
            try {
                loadFragment(HomeFragment(), "Home")
            } catch (e: Throwable) {
                // Ignore securely
            }
        }
    }

    private fun setupBottomNavigation() {
        binding?.bottomNav?.setOnItemSelectedListener { item ->
            try {
                when (item.itemId) {
                    R.id.nav_home -> {
                        loadFragment(HomeFragment(), "Home")
                        true
                    }
                    R.id.nav_history -> {
                        loadFragment(HistoryFragment(), "History")
                        true
                    }
                    R.id.nav_passbook -> {
                        loadFragment(PassbookFragment(), "Passbook")
                        true
                    }
                    R.id.nav_funds -> {
                        loadFragment(FundsFragment(), "Funds")
                        true
                    }
                    R.id.nav_support -> {
                        loadFragment(SupportFragment(), "Support")
                        true
                    }
                    else -> false
                }
            } catch (e: Throwable) {
                false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        if (isFinishing || isDestroyed) return
        if (currentFragmentTag == tag && supportFragmentManager.findFragmentByTag(tag) != null) {
            return
        }
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commitAllowingStateLoss()
            currentFragmentTag = tag
        } catch (e: Throwable) {
            // Ignore safely
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}