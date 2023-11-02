package com.mahmoudhamdyae.weatherforecast.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var todayAdapter: TodayAdapter
    private lateinit var nextDaysAdapter: NextDaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: HomeViewModel by viewModels()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        todayAdapter = TodayAdapter()
        nextDaysAdapter = NextDaysAdapter()
        binding.todayAdapter = todayAdapter
        binding.nextDaysAdapter = nextDaysAdapter

        viewModel.weather.observe(this) {
            todayAdapter.submitList(it?.weatherDataPerDay?.get(0))
            nextDaysAdapter.submitList(it?.daily)
        }

        viewModel.isFirstTime.observe(this) {
            if (it) Toast.makeText(requireContext(), "First time", Toast.LENGTH_SHORT).show()
            else Toast.makeText(requireContext(), "Not first time", Toast.LENGTH_SHORT).show()
        }


        viewModel.getWeather(33.23, 31.32)
    }
}