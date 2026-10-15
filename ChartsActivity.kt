package com.rama77.app.ui.charts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rama77.app.databinding.ActivityChartsBinding

class ChartsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarCharts.setNavigationOnClickListener {
            finish()
        }
    }
}