package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.domain.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertsViewModel @Inject constructor(
): ViewModel() {

    private var _alarms = MutableLiveData(listOf<Alarm>())
    val alarms: LiveData<List<Alarm>>
        get() = _alarms

    init {
        getAlarms()
    }

    private fun getAlarms() {
        viewModelScope.launch {
            _alarms.postValue(
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