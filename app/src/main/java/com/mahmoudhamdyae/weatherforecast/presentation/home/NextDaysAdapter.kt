package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.ItemNextDaysBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherDaily

class NextDaysAdapter: ListAdapter<WeatherDaily, NextDaysAdapter.ViewHolder>(WeatherDataDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNextDaysBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_next_days, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.weatherDaily = getItem(position)
    }

    inner class ViewHolder(var binding: ItemNextDaysBinding): RecyclerView.ViewHolder(binding.root)

    class WeatherDataDiffUtil: DiffUtil.ItemCallback<WeatherDaily>() {
        override fun areItemsTheSame(oldItem: WeatherDaily, newItem: WeatherDaily): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WeatherDaily, newItem: WeatherDaily): Boolean {
            return oldItem == newItem
        }

    }
}