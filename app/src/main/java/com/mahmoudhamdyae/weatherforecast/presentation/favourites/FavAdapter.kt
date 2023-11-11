package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.ItemFavBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Location

class FavAdapter(
    private val onDelAction: (Location) -> Unit,
    private val onClickAction: (Location) -> Unit,
): ListAdapter<Location, FavAdapter.ViewHolder>(FavDataDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFavBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_fav, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentPosition = getItem(position)
        if (currentPosition.name == "") currentPosition = currentPosition.copy(name = "No Name")
        holder.binding.location = currentPosition
        holder.binding.delFav.setOnClickListener { onDelAction(currentPosition) }
        holder.binding.cardWeather.setOnClickListener { onClickAction(currentPosition) }
    }

    inner class ViewHolder(var binding: ItemFavBinding): RecyclerView.ViewHolder(binding.root)

    class FavDataDiffUtil: DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }

    }
}