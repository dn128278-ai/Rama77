package com.rama77.app.ui.rates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rama77.app.databinding.ActivityGameRatesBinding

class GameRatesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameRatesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameRatesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}