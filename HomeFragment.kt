package com.rama77.app.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rama77.app.adapter.MarketAdapter
import com.rama77.app.databinding.FragmentHomeBinding
import com.rama77.app.model.MarketItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private var marketAdapter: MarketAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            binding?.root
        } catch (e: Throwable) {
            inflater.inflate(com.rama77.app.R.layout.fragment_home, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            setupQuickContact()
        } catch (e: Throwable) {
            android.util.Log.e("HomeFragment", "setupQuickContact error: ${e.message}")
        }

        try {
            setupMarketsList()
        } catch (e: Throwable) {
            android.util.Log.e("HomeFragment", "setupMarketsList error: ${e.message}")
        }
    }

    private fun setupQuickContact() {
        binding?.btnCallNow?.setOnClickListener {
            val ctx = context ?: return@setOnClickListener
            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:6377972674"))
                ctx.startActivity(intent)
            } catch (e: Throwable) {
                Toast.makeText(ctx, "Phone dialer unavailable", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.btnWhatsapp?.setOnClickListener {
            val ctx = context ?: return@setOnClickListener
            try {
                val url = "https://api.whatsapp.com/send?phone=916377972674&text=Hello%20Rama77%20Support"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ctx.startActivity(intent)
            } catch (e: Throwable) {
                Toast.makeText(ctx, "WhatsApp not installed on this device", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.btnBombayStarline?.setOnClickListener {
            context?.let { ctx ->
                Toast.makeText(ctx, "Bombay Starline Selected", Toast.LENGTH_SHORT).show()
            }
        }

        binding?.btnBombayJackpot?.setOnClickListener {
            context?.let { ctx ->
                Toast.makeText(ctx, "Bombay Jackpot Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMarketsList() {
        try {
            val markets = listOf(
                MarketItem("1", "DEVI MORNING", "268", "60", "145", "08:55 AM", "09:55 AM", "Closed for Today", false),
                MarketItem("2", "SURYA MORNING", "490", "38", "468", "10:05 AM", "11:05 AM", "Closed for Today", false),
                MarketItem("3", "SRIDEVI", "147", "22", "589", "11:40 AM", "12:42 PM", "Closed for Today", false),
                MarketItem("4", "TIME BAZAR", "670", "33", "580", "01:05 PM", "02:05 PM", "Closed for Today", false),
                MarketItem("5", "MADHUR DAY", "369", "8*", "***", "01:35 PM", "02:35 PM", "Running for Close", true),
                MarketItem("6", "SRIDEVI NIGHT", "290", "17", "188", "07:20 PM", "08:20 PM", "Running for Open", true),
                MarketItem("7", "MADHUR NIGHT", "567", "81", "146", "08:30 PM", "10:32 PM", "Running for Open", true),
                MarketItem("8", "MILAN NIGHT", "490", "37", "340", "09:05 PM", "11:05 PM", "Running for Open", true)
            )

            marketAdapter = MarketAdapter(markets) { market ->
                val ctx = context ?: return@MarketAdapter
                try {
                    val intent = Intent(ctx, com.rama77.app.ui.gamecenter.GameCenterActivity::class.java).apply {
                        putExtra("EXTRA_MARKET_ID", market.id)
                        putExtra("EXTRA_MARKET_NAME", market.name)
                        putExtra("EXTRA_OPEN_TIME", market.openTime)
                        putExtra("EXTRA_CLOSE_TIME", market.closeTime)
                    }
                    ctx.startActivity(intent)
                } catch (e: Throwable) {
                    Toast.makeText(ctx, "Opening market game...", Toast.LENGTH_SHORT).show()
                }
            }

            binding?.rvMarkets?.apply {
                val ctx = context ?: return@apply
                layoutManager = LinearLayoutManager(ctx)
                adapter = marketAdapter
            }
        } catch (e: Throwable) {
            android.util.Log.e("HomeFragment", "setupMarketsList error: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}