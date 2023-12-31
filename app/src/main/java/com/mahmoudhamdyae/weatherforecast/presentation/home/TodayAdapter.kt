package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.ItemHourlyWeatherBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.WeatherData

class TodayAdapter: ListAdapter<WeatherData, TodayAdapter.ViewHolder>(WeatherDataDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHourlyWeatherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_hourly_weather, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.weatherData = current
//        current?.let { current1 ->
//            holder.binding.time.text = "${current1.time.toLocalTime().hour}"//:${current.time.minute}"
//        }
    }

    inner class ViewHolder(var binding: ItemHourlyWeatherBinding): RecyclerView.ViewHolder(binding.root)

    class WeatherDataDiffUtil: DiffUtil.ItemCallback<WeatherData>() {
        override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
            return oldItem == newItem
        }

    }
}