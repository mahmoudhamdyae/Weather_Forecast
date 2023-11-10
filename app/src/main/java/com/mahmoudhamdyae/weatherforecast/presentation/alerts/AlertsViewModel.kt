package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.domain.model.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
): ViewModel() {

    private var _alarms = MutableStateFlow(listOf<Alarm>())
    val alarms = _alarms.asStateFlow()

    init {
        getAlarms()
    }

    private fun getAlarms() {
        viewModelScope.launch {
            _alarms.value =
                listOf(
                    Alarm(
                        fromDay = 23,
                        fromHour = 13,
                        fromMin = 3,
                        fromMonth = 3,
                        fromYear = 2023,
                        toDay = 24,
                        toHour = 14,
                        toMin = 4,
                        toMonth = 3,
                        toYear = 2023,
                        isAlarm = true
                    )
                )
        }
    }

    fun addAlarm(alarm: Alarm) {
    }

    fun delAlarm(alarm: Alarm) {
        viewModelScope.launch {
            //
        }
    }
}