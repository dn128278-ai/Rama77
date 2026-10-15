package com.rama77.app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.rama77.app.MainActivity
import com.rama77.app.R
import com.rama77.app.databinding.ActivityLoginBinding
import com.rama77.app.ui.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding
    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private var isBiometricReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Safe ViewBinding initialization with direct resource fallback
        try {
            _binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding?.root)
        } catch (e: Throwable) {
            Log.e("LoginActivity", "Failed to inflate ActivityLoginBinding, fallback to layout", e)
            setContentView(R.layout.activity_login)
        }

        // 2. Safe Fullscreen / Insets handling (Never crashes on custom Android skins)
        try {
            enableFullscreenMode()
        } catch (e: Throwable) {
            Log.w("LoginActivity", "Fullscreen mode ignored safely: ${e.message}")
        }

        // 3. Safe SharedPreferences verification (guarded against SecurityException on native storage)
        try {
            val prefs = getSharedPreferences("RAMA77_PREFS", Context.MODE_PRIVATE)
            val savedPhone = prefs.getString("KEY_USER_PHONE", null)
            if (!savedPhone.isNullOrEmpty()) {
                Log.d("LoginActivity", "Existing registered account found")
            }
        } catch (se: SecurityException) {
            Log.w("LoginActivity", "SecurityException reading preferences: ${se.message}")
        } catch (e: Throwable) {
            Log.w("LoginActivity", "SharedPreferences read error ignored: ${e.message}")
        }

        // 4. Safe PIN Auto-Advance Setup
        try {
            setupPinAutoAdvance()
        } catch (e: Throwable) {
            Log.w("LoginActivity", "Pin setup error ignored: ${e.message}")
        }

        // 5. Safe Biometrics initialization with hardware and enrollment validation
        try {
            setupBiometrics()
        } catch (e: Throwable) {
            Log.w("LoginActivity", "Biometrics setup bypassed safely: ${e.message}")
        }

        // 6. Safe Click Listeners Setup
        try {
            setupClickListeners()
        } catch (e: Throwable) {
            Log.w("LoginActivity", "Click listener setup error ignored: ${e.message}")
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
                        Log.d("LoginActivity", "insetsController hide ignored: ${e.message}")
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
            }
        } catch (e: Throwable) {
            Log.w("LoginActivity", "enableFullscreenMode error ignored: ${e.message}")
        }
    }

    private fun setupPinAutoAdvance() {
        try {
            val pins = arrayOf(
                binding?.etPin1 ?: findViewById(R.id.etPin1),
                binding?.etPin2 ?: findViewById(R.id.etPin2),
                binding?.etPin3 ?: findViewById(R.id.etPin3),
                binding?.etPin4 ?: findViewById(R.id.etPin4)
            )
            for (i in pins.indices) {
                pins[i]?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (s?.length == 1 && i < pins.size - 1) {
                            pins[i + 1]?.requestFocus()
                        }
                    }
                    override fun afterTextChanged(s: Editable?) {}
                })
            }
        } catch (e: Throwable) {
            Log.w("LoginActivity", "setupPinAutoAdvance error ignored: ${e.message}")
        }
    }

    private fun setupBiometrics() {
        try {
            val biometricManager = BiometricManager.from(this)
            val canAuth = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK
            )
            if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
                isBiometricReady = false
                Log.i("LoginActivity", "Biometric not available on device (code: $canAuth)")
                return
            }

            executor = ContextCompat.getMainExecutor(this)
            val exec = executor ?: return

            biometricPrompt = BiometricPrompt(this, exec,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // Safe dispatching on Main Thread to prevent native UI thread blocking
                        lifecycleScope.launch(Dispatchers.Main) {
                            try {
                                // Background IO task for disk storage / SharedPreferences
                                withContext(Dispatchers.IO) {
                                    try {
                                        val prefs = getSharedPreferences("RAMA77_PREFS", Context.MODE_PRIVATE)
                                        prefs.edit().putBoolean("KEY_IS_LOGGED_IN", true).apply()
                                    } catch (se: SecurityException) {
                                        Log.w("LoginActivity", "SecurityException storing login flag: ${se.message}")
                                    } catch (e: Throwable) {
                                        Log.w("LoginActivity", "Storage error storing login flag: ${e.message}")
                                    }
                                }

                                Toast.makeText(this@LoginActivity, "Biometric Authenticated!", Toast.LENGTH_SHORT).show()
                                
                                // Clean standard task flags for Android 12+
                                val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                }
                                startActivity(intent)
                                finish()
                            } catch (e: Throwable) {
                                Log.e("LoginActivity", "Error starting MainActivity from biometric", e)
                                try {
                                    val fallbackIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    startActivity(fallbackIntent)
                                    finish()
                                } catch (err: Throwable) {
                                    Log.e("LoginActivity", "Fallback intent error: ${err.message}")
                                }
                            }
                        }
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        lifecycleScope.launch(Dispatchers.Main) {
                            try {
                                Toast.makeText(this@LoginActivity, "Fingerprint not recognized. Please retry or use MPIN.", Toast.LENGTH_SHORT).show()
                            } catch (e: Throwable) {
                                Log.w("LoginActivity", "Biometric fail toast error: ${e.message}")
                            }
                        }
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Log.w("LoginActivity", "Biometric authentication error $errorCode: $errString")
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("RAMA77 Biometric Login")
                .setSubtitle("Confirm your fingerprint to access your account")
                .setNegativeButtonText("Use MPIN")
                .build()

            isBiometricReady = true
        } catch (e: Throwable) {
            Log.w("LoginActivity", "setupBiometrics safely skipped: ${e.message}")
            isBiometricReady = false
            biometricPrompt = null
            promptInfo = null
        }
    }

    private fun setupClickListeners() {
        val btnLogin = binding?.btnLogin ?: findViewById(R.id.btnLogin)
        btnLogin?.setOnClickListener {
            performLogin()
        }

        val ivFingerprint = binding?.ivFingerprint ?: findViewById(R.id.ivFingerprint)
        ivFingerprint?.setOnClickListener {
            try {
                if (isBiometricReady && biometricPrompt != null && promptInfo != null) {
                    biometricPrompt?.authenticate(promptInfo!!)
                } else {
                    Toast.makeText(this, "Biometric sensor unavailable. Please enter 4-digit MPIN.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Throwable) {
                Log.w("LoginActivity", "Biometric prompt trigger error: ${e.message}")
                Toast.makeText(this, "Please enter 4-digit MPIN to login", Toast.LENGTH_SHORT).show()
            }
        }

        val tvRegisterNow = binding?.tvRegisterNow ?: findViewById(R.id.tvRegisterNow)
        tvRegisterNow?.setOnClickListener {
            try {
                startActivity(Intent(this, RegisterActivity::class.java))
            } catch (e: Throwable) {
                Log.e("LoginActivity", "Failed to launch RegisterActivity", e)
            }
        }

        val tvForgotMpin = binding?.tvForgotMpin ?: findViewById(R.id.tvForgotMpin)
        tvForgotMpin?.setOnClickListener {
            Toast.makeText(this, "Please contact WhatsApp Support to reset your MPIN", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        try {
            binding?.btnLogin?.text = ""
            binding?.pbLoginLoading?.visibility = View.VISIBLE

            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    // Safe background IO thread delay & storage to prevent UI freeze on physical device
                    withContext(Dispatchers.IO) {
                        delay(600) // Simulating network auth without lagging main thread

                        try {
                            val prefs = getSharedPreferences("RAMA77_PREFS", Context.MODE_PRIVATE)
                            prefs.edit().putBoolean("KEY_IS_LOGGED_IN", true).apply()
                        } catch (se: SecurityException) {
                            Log.w("LoginActivity", "SecurityException persisting login: ${se.message}")
                        } catch (e: Throwable) {
                            Log.w("LoginActivity", "Failed to persist login state: ${e.message}")
                        }
                    }

                    // Return to Dispatchers.Main for UI feedback
                    binding?.pbLoginLoading?.visibility = View.GONE
                    binding?.btnLogin?.text = "Login"

                    // Standard intent transition with task-clearing flags for Android 12+
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(intent)
                    finish()
                } catch (e: Throwable) {
                    Log.e("LoginActivity", "Coroutine login error, executing fallback", e)
                    binding?.pbLoginLoading?.visibility = View.GONE
                    binding?.btnLogin?.text = "Login"
                    val fallbackIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    startActivity(fallbackIntent)
                    finish()
                }
            }
        } catch (e: Throwable) {
            Log.e("LoginActivity", "performLogin error, fallback to MainActivity", e)
            val fallbackIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(fallbackIntent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}