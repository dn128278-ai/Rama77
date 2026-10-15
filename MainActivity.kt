package com.rama77.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.rama77.app.databinding.ActivityMainBinding
import com.rama77.app.ui.charts.ChartsActivity
import com.rama77.app.ui.funds.FundsFragment
import com.rama77.app.ui.history.HistoryFragment
import com.rama77.app.ui.home.HomeFragment
import com.rama77.app.ui.mpin.ChangeMpinActivity
import com.rama77.app.ui.passbook.PassbookFragment
import com.rama77.app.ui.rates.GameRatesActivity
import com.rama77.app.ui.settings.SettingsActivity
import com.rama77.app.ui.support.SupportFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding
    private var currentFragmentTag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Inflate ViewBinding safely and set content view
        try {
            _binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(_binding?.root)
        } catch (e: Throwable) {
            Log.e("MainActivity", "Failed to inflate ActivityMainBinding, fallback to layout", e)
            setContentView(R.layout.activity_main)
        }

        // 2. Play Integrity / Root Detection Check (Guarded against SecurityException)
        try {
            checkDeviceIntegrity()
        } catch (e: Throwable) {
            Log.w("MainActivity", "checkDeviceIntegrity error ignored: ${e.message}")
        }

        // 3. UI Setup
        try {
            setupToolbar()
        } catch (e: Throwable) {
            Log.e("MainActivity", "setupToolbar error: ${e.message}")
        }

        try {
            setupBottomNav()
        } catch (e: Throwable) {
            Log.e("MainActivity", "setupBottomNav error: ${e.message}")
        }

        try {
            setupDrawer()
        } catch (e: Throwable) {
            Log.e("MainActivity", "setupDrawer error: ${e.message}")
        }

        // 4. Initial fragment load (only when fresh instance, avoiding double-transactions)
        if (savedInstanceState == null) {
            try {
                loadFragment(HomeFragment(), "Home")
            } catch (e: Throwable) {
                Log.e("MainActivity", "Failed to load initial HomeFragment: ${e.message}")
            }
        }
    }

    private fun checkDeviceIntegrity() {
        try {
            val rootPaths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su"
            )
            val isRooted = rootPaths.any { path ->
                try {
                    val file = java.io.File(path)
                    file.exists()
                } catch (se: SecurityException) {
                    false
                } catch (e: Throwable) {
                    false
                }
            } || (android.os.Build.TAGS?.contains("test-keys") == true)
            
            if (isRooted) {
                Toast.makeText(this, "Notice: Custom Android ROM detected", Toast.LENGTH_SHORT).show()
            }
        } catch (se: SecurityException) {
            Log.w("MainActivity", "SecurityException during root integrity check: ${se.message}")
        } catch (e: Throwable) {
            Log.w("MainActivity", "Root check bypassed: ${e.message}")
        }
    }

    private fun setupToolbar() {
        val btnMenuDrawer = binding?.btnMenuDrawer ?: findViewById(R.id.btnMenuDrawer)
        btnMenuDrawer?.setOnClickListener {
            try {
                val drawer = binding?.drawerLayout ?: findViewById(R.id.drawerLayout)
                drawer?.openDrawer(GravityCompat.START)
            } catch (e: Throwable) {
                Log.e("MainActivity", "Drawer open error: ${e.message}")
            }
        }

        val btnWalletChip = binding?.btnWalletChip ?: findViewById(R.id.btnWalletChip)
        btnWalletChip?.setOnClickListener {
            try {
                loadFragment(FundsFragment(), "Funds")
                binding?.bottomNav?.selectedItemId = R.id.nav_funds
            } catch (e: Throwable) {
                Log.e("MainActivity", "Wallet chip click error: ${e.message}")
            }
        }

        val btnNotificationBell = binding?.btnNotificationBell ?: findViewById(R.id.btnNotificationBell)
        btnNotificationBell?.setOnClickListener {
            try {
                startActivity(Intent(this, SettingsActivity::class.java))
            } catch (e: Throwable) {
                Log.e("MainActivity", "Notification bell click error: ${e.message}")
            }
        }
    }

    private fun setupBottomNav() {
        binding?.bottomNav?.setOnItemSelectedListener { item ->
            try {
                when (item.itemId) {
                    R.id.nav_history -> loadFragment(HistoryFragment(), "History")
                    R.id.nav_passbook -> loadFragment(PassbookFragment(), "Passbook")
                    R.id.nav_home -> loadFragment(HomeFragment(), "Home")
                    R.id.nav_funds -> loadFragment(FundsFragment(), "Funds")
                    R.id.nav_support -> loadFragment(SupportFragment(), "Support")
                    else -> false
                }
                true
            } catch (e: Throwable) {
                Log.e("MainActivity", "BottomNav selection error: ${e.message}")
                false
            }
        }
    }

    private fun setupDrawer() {
        binding?.navigationView?.setNavigationItemSelectedListener(this)
    }

    private fun loadFragment(fragment: Fragment, tag: String) {
        if (isFinishing || isDestroyed) return
        if (currentFragmentTag == tag && supportFragmentManager.findFragmentByTag(tag) != null) {
            return
        }
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
            currentFragmentTag = tag
        } catch (e: Throwable) {
            Log.e("MainActivity", "Failed to load fragment: $tag", e)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        try {
            val drawer = binding?.drawerLayout ?: findViewById(R.id.drawerLayout)
            drawer?.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.drawer_home -> {
                    loadFragment(HomeFragment(), "Home")
                    binding?.bottomNav?.selectedItemId = R.id.nav_home
                }
                R.id.drawer_charts -> startActivity(Intent(this, ChartsActivity::class.java))
                R.id.drawer_change_mpin -> startActivity(Intent(this, ChangeMpinActivity::class.java))
                R.id.drawer_game_rates -> startActivity(Intent(this, GameRatesActivity::class.java))
                R.id.drawer_settings -> startActivity(Intent(this, SettingsActivity::class.java))
                R.id.drawer_share_app -> shareApp()
                R.id.drawer_exit -> finish()
                else -> Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Throwable) {
            Log.e("MainActivity", "Navigation drawer item error: ${e.message}")
        }
        return true
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey 👋 I am Using Rama 77 Application to track and play games! Download now."
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Sharing text"))
        } catch (e: Throwable) {
            Log.e("MainActivity", "Share app error: ${e.message}")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        try {
            val drawer = binding?.drawerLayout ?: findViewById(R.id.drawerLayout)
            if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        } catch (e: Throwable) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}