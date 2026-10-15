package com.rama77.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rama77.app.R
import com.rama77.app.databinding.ItemSeriesTabBinding

class SeriesTabAdapter(
    private val tabs: List<String> = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
    private val onTabSelected: (String) -> Unit = {}
) : RecyclerView.Adapter<SeriesTabAdapter.SeriesTabViewHolder>() {

    private var selectedPosition = 0

    inner class SeriesTabViewHolder(val binding: ItemSeriesTabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesTabViewHolder {
        val binding = ItemSeriesTabBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SeriesTabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeriesTabViewHolder, position: Int) {
        val tab = tabs[position]
        holder.binding.tvSeriesTab.text = tab
        val isSelected = position == selectedPosition
        holder.binding.tvSeriesTab.setBackgroundResource(
            if (isSelected) R.drawable.bg_page_active_orange else R.drawable.bg_page_inactive
        )
        holder.binding.tvSeriesTab.setTextColor(
            ContextCompat.getColor(
                holder.itemView.context,
                if (isSelected) android.R.color.white else R.color.text_slate_dark
            )
        )
        holder.itemView.setOnClickListener {
            val prev = selectedPosition
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(prev)
            notifyItemChanged(selectedPosition)
            onTabSelected(tab)
        }
    }

    override fun getItemCount(): Int = tabs.size

    fun getSelectedTab(): String = if (selectedPosition in tabs.indices) tabs[selectedPosition] else ""
}