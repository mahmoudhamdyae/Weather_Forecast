package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.alarm.AlarmItem
import com.mahmoudhamdyae.weatherforecast.alarm.AlarmScheduler
import com.mahmoudhamdyae.weatherforecast.alarm.AlarmSchedulerImpl
import com.mahmoudhamdyae.weatherforecast.alarm.AlarmType
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentAlertsBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Alarm
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding

    private lateinit var adapter: AlertsAdapter
    private lateinit var viewModel: AlertsViewModel

    private var hourOfDay: Int? = null
    private var minute: Int? = null
    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null
    private var alarmType = AlarmType.ALARM

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
        binding.addFab.setOnClickListener { showDatePicker() }

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

    private fun showDatePicker() {
            val calender = Calendar.getInstance()
            val currentYear = calender.get(Calendar.YEAR)
            val currentMonth = calender.get(Calendar.MONTH)
            val currentDay = calender.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    this.year = year
                    this.month = monthOfYear
                    this.day = dayOfMonth
                    showTimePicker()
                },
                currentYear,
                currentMonth,
                currentDay
            )
            datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calender = Calendar.getInstance()
        val currentHour = calender.get(Calendar.HOUR)
        val currentMinute = calender.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(), { _, hour, minute ->
                this.hourOfDay = hour
                this.minute = minute
                showAlarmOrNotificationAlarm()
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun showAlarmOrNotificationAlarm() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                showLabelDialog()
            }
            .setNegativeButton(getString(R.string.label_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setSingleChoiceItems(
                arrayOf(
                    getString(R.string.alarm_or_notifications_dialog_alarm),
                    getString(R.string.alarm_or_notifications_dialog_notification)
                ), 0
            ) { _, which ->
                if (which == 0) {
                    alarmType = AlarmType.ALARM
                } else if (which == 1) {
                    alarmType = AlarmType.NOTIFICATION
                }
            }

        builder.create().apply {
            setCancelable(false)
            show()
        }
    }

    private fun showLabelDialog() {

        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_add_message)

        dialog.findViewById<Button>(R.id.ok_button).setOnClickListener {
            val message = dialog.findViewById<EditText>(R.id.label).text.toString()
            dialog.dismiss()
            setAlarm(message)
        }

        dialog.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setAlarm(message: String) {
        val alarmScheduler : AlarmScheduler = AlarmSchedulerImpl(requireContext())
        val localDateTime = LocalDateTime.of(year!!, month!!, day!!, hourOfDay!!, minute!!)
        val alarmItem = AlarmItem(
            alarmTime = localDateTime,
            message = message,
            alarmType = alarmType
        )
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        alarmScheduler.schedule(alarmItem)
    }
}