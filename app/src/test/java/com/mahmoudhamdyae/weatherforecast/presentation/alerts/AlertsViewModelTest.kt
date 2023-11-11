package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import com.mahmoudhamdyae.weatherforecast.FakeRepository
import com.mahmoudhamdyae.weatherforecast.MainCoroutineRule
import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlertsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AlertsViewModel
    private lateinit var repo: Repository

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = AlertsViewModel(repo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarms_addAlarm_getInFlow() = runBlockingTest {
        // GIVEN: new alarm object
        val alarm = Alarm(1, 2, 3, 4, 5, true)

        // When: add new alarm
        viewModel.addAlarm(alarm)
        viewModel.getAlarmsList()

        // Then: Get the alarm
        Assert.assertTrue(viewModel.alarms.first().contains(alarm))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarms_delAlarm_getInFlowNotExist() = runBlockingTest {
        // GIVEN: new alarm object
        val alarm = Alarm(1, 2, 3, 4, 5, true)

        // When: add new alarm
        viewModel.addAlarm(alarm)
        viewModel.delAlarm(alarm)
        viewModel.getAlarmsList()

        // Then: Get the alarm
        Assert.assertTrue(!viewModel.alarms.first().contains(alarm))
    }
}