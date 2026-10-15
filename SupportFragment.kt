package com.rama77.app.ui.support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rama77.app.databinding.FragmentSupportBinding

class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentSupportBinding.inflate(inflater, container, false)
            binding?.root
        } catch (e: Throwable) {
            inflater.inflate(com.rama77.app.R.layout.fragment_support, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cardDepositIssue?.setOnClickListener {
            openWhatsAppSupport("Hi RAMA77, I have an issue with my Deposit.")
        }

        binding?.cardWithdrawIssue?.setOnClickListener {
            openWhatsAppSupport("Hi RAMA77, I have an issue with my Withdrawal.")
        }

        binding?.cardOtherIssue?.setOnClickListener {
            openWhatsAppSupport("Hi RAMA77, I need assistance with my account.")
        }
    }

    private fun openWhatsAppSupport(message: String) {
        val ctx = context ?: return
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=916377972674&text=" + Uri.encode(message))
            }
            ctx.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(ctx, "WhatsApp not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}