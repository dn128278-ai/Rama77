package com.rama77.app.ui.funds

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rama77.app.R
import com.rama77.app.databinding.FragmentFundsBinding

class FundsFragment : Fragment() {

    private var _binding: FragmentFundsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            _binding = FragmentFundsBinding.inflate(inflater, container, false)
            binding?.root
        } catch (e: Throwable) {
            inflater.inflate(com.rama77.app.R.layout.fragment_funds, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            binding?.tvFundsBalance?.text = "₹753"

            binding?.btnAddCash?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Minimum deposit limit Rs. 300", Toast.LENGTH_SHORT).show()
                }
            }

            binding?.btnWithdrawCash?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Withdrawals processed 24/7", Toast.LENGTH_SHORT).show()
                }
            }

            setupMenuItems()
        } catch (e: Throwable) {
            android.util.Log.e("FundsFragment", "onViewCreated error: ${e.message}")
        }
    }

    private fun setupMenuItems() {
        try {
            // 1. Add Funds
            binding?.itemAddFunds?.menuTitle?.text = "Add Funds"
            binding?.itemAddFunds?.menuIcon?.setImageResource(R.drawable.ic_wallet_funds)
            binding?.itemAddFunds?.cardFundsMenu?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Add Funds clicked", Toast.LENGTH_SHORT).show()
                }
            }

            // 2. Add Funds History
            binding?.itemAddFundsHistory?.menuTitle?.text = "Add Funds History"
            binding?.itemAddFundsHistory?.menuIcon?.setImageResource(R.drawable.ic_book_passbook)
            binding?.itemAddFundsHistory?.cardFundsMenu?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Deposit History clicked", Toast.LENGTH_SHORT).show()
                }
            }

            // 3. Withdraw Funds
            binding?.itemWithdrawFunds?.menuTitle?.text = "Withdraw Funds"
            binding?.itemWithdrawFunds?.menuIcon?.setImageResource(R.drawable.ic_wallet)
            binding?.itemWithdrawFunds?.cardFundsMenu?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Withdraw Funds clicked", Toast.LENGTH_SHORT).show()
                }
            }

            // 4. Withdraw Funds History
            binding?.itemWithdrawFundsHistory?.menuTitle?.text = "Withdraw Funds History"
            binding?.itemWithdrawFundsHistory?.menuIcon?.setImageResource(R.drawable.ic_clock_history)
            binding?.itemWithdrawFundsHistory?.cardFundsMenu?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Withdraw History clicked", Toast.LENGTH_SHORT).show()
                }
            }

            // 5. Add Bank Details
            binding?.itemAddBankDetails?.menuTitle?.text = "Add Bank Details"
            binding?.itemAddBankDetails?.menuIcon?.setImageResource(R.drawable.ic_wallet_funds)
            binding?.itemAddBankDetails?.cardFundsMenu?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Bank Details clicked", Toast.LENGTH_SHORT).show()
                }
            }

            // 6. Bank Details History
            binding?.itemBankDetailsHistory?.menuTitle?.text = "Bank Details History"
            binding?.itemBankDetailsHistory?.menuIcon?.setImageResource(R.drawable.ic_book_passbook)
            binding?.itemBankDetailsHistory?.cardFundsMenu?.setOnClickListener {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Bank History clicked", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Throwable) {
            android.util.Log.e("FundsFragment", "setupMenuItems error: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}