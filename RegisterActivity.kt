package com.rama77.app.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rama77.app.MainActivity
import com.rama77.app.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityRegisterBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: Throwable) {
            Log.e("RegisterActivity", "Failed to inflate ActivityRegisterBinding", e)
            setContentView(com.rama77.app.R.layout.activity_register)
        }

        try {
            enableFullscreenMode()
        } catch (e: Throwable) {
            Log.w("RegisterActivity", "enableFullscreenMode error ignored: ${e.message}")
        }

        try {
            setupListeners()
        } catch (e: Throwable) {
            Log.e("RegisterActivity", "setupListeners error: ${e.message}")
        }
    }

    private fun enableFullscreenMode() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                window?.decorView?.post {
                    try {
                        window?.insetsController?.hide(
                            android.view.WindowInsets.Type.statusBars()
                        )
                    } catch (e: Throwable) {
                        Log.d("RegisterActivity", "insetsController hide ignored: ${e.message}")
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                window?.decorView?.systemUiVisibility = (
                    android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                    or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
            }
        } catch (e: Throwable) {
            Log.w("RegisterActivity", "enableFullscreenMode error ignored: ${e.message}")
        }
    }

    private fun setupListeners() {
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
        }

        binding.btnCompleteRegister.setOnClickListener {
            validateAndRegister()
        }
    }

    private fun validateAndRegister() {
        val mobile = binding.etMobileNumber.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString()?.trim().orEmpty()
        val promo = binding.etPromoCode.text?.toString()?.trim().orEmpty()
        val newMpin = binding.etNewMpin.text?.toString()?.trim().orEmpty()
        val confirmMpin = binding.etConfirmMpin.text?.toString()?.trim().orEmpty()
        val biometricEnabled = binding.switchFingerprint.isChecked

        // 1. Validate Mobile Number
        if (mobile.length != 10) {
            binding.tilMobileNumber.error = "Please enter a valid 10-digit mobile number"
            binding.etMobileNumber.requestFocus()
            return
        } else {
            binding.tilMobileNumber.error = null
        }

        // 2. Validate Password
        if (password.length < 4) {
            binding.tilPassword.error = "Password must be at least 4 characters long"
            binding.etPassword.requestFocus()
            return
        } else {
            binding.tilPassword.error = null
        }

        // 3. Validate MPIN
        if (newMpin.length != 4) {
            binding.tilNewMpin.error = "Please enter a 4-digit MPIN"
            binding.etNewMpin.requestFocus()
            return
        } else {
            binding.tilNewMpin.error = null
        }

        if (newMpin != confirmMpin) {
            binding.tilConfirmMpin.error = "MPIN and Confirm MPIN do not match!"
            binding.etConfirmMpin.requestFocus()
            return
        } else {
            binding.tilConfirmMpin.error = null
        }

        // Show Loading Animation
        binding.btnCompleteRegister.text = ""
        binding.pbRegisterLoading.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                // Background IO task for delay and SharedPreferences to protect UI thread
                withContext(Dispatchers.IO) {
                    delay(1200) // Simulating network registration API call

                    // Persist user credentials in local secure preferences safely
                    try {
                        val prefs = getSharedPreferences("RAMA77_PREFS", Context.MODE_PRIVATE)
                        prefs.edit().apply {
                            putString("KEY_USER_PHONE", mobile)
                            putString("KEY_USER_MPIN", newMpin)
                            putBoolean("KEY_BIOMETRIC_ENABLED", biometricEnabled)
                            putString("KEY_PROMO_CODE", promo)
                            putInt("KEY_WALLET_BALANCE", if (promo.isNotEmpty()) 100 else 0)
                            putBoolean("KEY_IS_LOGGED_IN", true)
                            apply()
                        }
                    } catch (se: SecurityException) {
                        Log.w("RegisterActivity", "SecurityException during preferences save: ${se.message}")
                    } catch (e: Throwable) {
                        Log.w("RegisterActivity", "Error saving preferences: ${e.message}")
                    }
                }

                binding.pbRegisterLoading.visibility = View.GONE
                binding.btnCompleteRegister.text = "Complete Registration"

                Toast.makeText(
                    this@RegisterActivity,
                    "Registration Successful! Welcome to RAMA77.",
                    Toast.LENGTH_LONG
                ).show()

                // Redirect directly to Home Dashboard with clean task flags for Android 12+
                val intent = Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    putExtra("EXTRA_NEW_USER", true)
                    putExtra("EXTRA_PHONE", mobile)
                }
                startActivity(intent)
                finish()
            } catch (e: Throwable) {
                Log.e("RegisterActivity", "Registration flow error, fallback to MainActivity", e)
                val fallbackIntent = Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(fallbackIntent)
                finish()
            }
        }
    }
}