package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.ItemAlarmBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Alarm

class AlertsAdapter(
    private val onDelAction: (Alarm) -> Unit
): ListAdapter<Alarm, AlertsAdapter.ViewHolder>(AlertsDataDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlarmBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_alarm, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPosition = getItem(position)
        holder.binding.alarm = currentPosition
        holder.binding.delAlarm.setOnClickListener {
            onDelAction(currentPosition)
        }
    }

    inner class ViewHolder(var binding: ItemAlarmBinding): RecyclerView.ViewHolder(binding.root)

    class AlertsDataDiffUtil: DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }

    }
}