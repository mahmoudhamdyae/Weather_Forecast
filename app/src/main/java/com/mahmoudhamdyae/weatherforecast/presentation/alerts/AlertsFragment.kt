package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentAlertsBinding
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentFavBinding

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}