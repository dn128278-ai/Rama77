package com.rama77.app.ui.passbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rama77.app.R
import com.rama77.app.databinding.FragmentPassbookBinding

class PassbookFragment : Fragment() {

    private var _binding: FragmentPassbookBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentPassbookBinding.inflate(inflater, container, false)
            binding?.root
        } catch (e: Throwable) {
            inflater.inflate(R.layout.fragment_passbook, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            // Ensure proper ViewBinding or findViewById for tvPassbookBalance
            val tvBalance: TextView? = view.findViewById(R.id.tvPassbookBalance)
            tvBalance?.text = "₹753"
            binding?.tvPassbookBalance?.text = "₹753"
        } catch (e: Throwable) {
            android.util.Log.e("PassbookFragment", "onViewCreated error: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}