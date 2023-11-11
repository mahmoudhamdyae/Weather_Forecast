package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import com.mahmoudhamdyae.weatherforecast.FakeRepository
import com.mahmoudhamdyae.weatherforecast.MainCoroutineRule
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FavViewModel
    private lateinit var repo: Repository

    @Before
    fun setUp() {
        repo = FakeRepository()
        viewModel = FavViewModel(repo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather_addFav_getInFlow() = runBlockingTest {
        // GIVEN: new location object
        val location = Location(31.6, 43.3, "name", "123")

        // When: add new location
        viewModel.addFav(location)
        viewModel.getFavList()

        // Then: Get the location
        assertTrue(viewModel.fav.first().contains(location))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getWeather_delFav_getInFlowNotExist() = runBlockingTest {
        // GIVEN: new location object
        val location = Location(31.6, 43.3, "name", "123")

        // When: add new location
        viewModel.addFav(location)
        viewModel.delFav(location)
        viewModel.getFavList()

        // Then: Get the location
        assertTrue(!viewModel.fav.first().contains(location))
    }
}