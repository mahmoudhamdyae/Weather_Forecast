package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class AlertsViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertsViewModel(repository) as T
    }
}

class AlertsViewModel(
    private val repository: Repository
): ViewModel() {

    private var _alarms = MutableStateFlow(listOf<Alarm>())
    val alarms = _alarms.asStateFlow()

    init {
        getAlarms()
    }

    private fun getAlarms() {
        viewModelScope.launch {
            repository.getAlarms().collect {
                _alarms.value = it
            }
        }
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch {
            repository.insertAlarm(alarm)
        }
    }

    fun delAlarm(alarm: Alarm) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }
}