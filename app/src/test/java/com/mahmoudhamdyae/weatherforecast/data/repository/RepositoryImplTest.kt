package com.mahmoudhamdyae.weatherforecast.data.repository

import com.mahmoudhamdyae.weatherforecast.data.local.FakeLocalDataSource
import com.mahmoudhamdyae.weatherforecast.data.local.LocalDataSource
import com.mahmoudhamdyae.weatherforecast.data.local.model.Alarm
import com.mahmoudhamdyae.weatherforecast.data.mappers.toWeatherInfo
import com.mahmoudhamdyae.weatherforecast.data.remote.FakeRemoteDataSource
import com.mahmoudhamdyae.weatherforecast.data.remote.RemoteDataSource
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class RepositoryImplTest {

    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var localDataSource: LocalDataSource
    private lateinit var repository: Repository

    private val location = Location(31.2, 23.2, "Mansoura")
    private val alarm = Alarm(1,2, 3, 4, 5, true)

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteDataSource()
        localDataSource = FakeLocalDataSource(locations = mutableListOf(location))

        repository = RepositoryImpl(remoteDataSource, localDataSource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather_LocalDataEqualsRemote() = runBlockingTest {
        repository.getWeather(31.2, 31.2, "indSpeed", "Ar")

        assertEquals(localDataSource.getWeather(), remoteDataSource.getWeather(31.2, 31.2, "indSpeed", "Ar").toWeatherInfo())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertLocation_assertExist() = runBlockingTest {
        repository.insertAlarm(alarm)

        assert(repository.getAlarms().first().contains(alarm))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delLocation_assertNotExist() = runBlockingTest {
        repository.insertLocation(location)
        repository.delLocation(location)

        assertFalse(repository.getLocations().first().contains(location))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAlarm_assertExist() = runBlockingTest {
        repository.insertLocation(location)

        assert(repository.getLocations().first().contains(location))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun delAlarm_assertNotExist() = runBlockingTest {
        repository.insertAlarm(alarm)
        repository.deleteAlarm(alarm)

        assertFalse(repository.getAlarms().first().contains(alarm))
    }
}