package com.rama77.app.ui.charts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rama77.app.databinding.ActivityChartDetailBinding

class ChartDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}