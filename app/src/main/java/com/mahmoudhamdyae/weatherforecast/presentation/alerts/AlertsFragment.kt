package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentAlertsBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Alarm
import kotlinx.coroutines.launch

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    private lateinit var adapter: AlertsAdapter
    private lateinit var viewModel: AlertsViewModel

            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alerts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[AlertsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        adapter = AlertsAdapter(::showDelDialog)
        binding.alarmAdapter = adapter
        binding.addFab.setOnClickListener { addAlarm() }

        lifecycleScope.launch { viewModel.alarms.collect(adapter::submitList) }
    }

    private fun showDelDialog(alarm: Alarm) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.dialog_del_alarm)
            .setPositiveButton(R.string.dialog_del_ok) { dialog, _ ->
                viewModel.delAlarm(alarm)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_del_cancel) { dialog, _ ->
                // User cancelled the dialog
                dialog.dismiss()
            }.show()
    }

    private fun addAlarm() {
        //
    }
}