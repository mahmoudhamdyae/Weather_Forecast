package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mahmoudhamdyae.weatherforecast.MainActivity
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.data.local.AppDatabase
import com.mahmoudhamdyae.weatherforecast.data.local.LocalDataSourceImpl
import com.mahmoudhamdyae.weatherforecast.data.remote.RemoteDataSourceImpl
import com.mahmoudhamdyae.weatherforecast.data.repository.RepositoryImpl
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentFavBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.presentation.map.LATITUDE
import com.mahmoudhamdyae.weatherforecast.presentation.map.LONGITUDE
import com.mahmoudhamdyae.weatherforecast.presentation.map.MapActivity
import com.mahmoudhamdyae.weatherforecast.presentation.map.NAME
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*

class FavFragment : Fragment() {

    private lateinit var binding: FragmentFavBinding

    private lateinit var adapter: FavAdapter

    private val viewModel: FavViewModel by lazy {
        val factory = FavViewModelFactory(
            RepositoryImpl.getRepository(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(
                    AppDatabase.getDatabase(requireContext())
                )
            )
        )
        ViewModelProvider(this, factory)[FavViewModel::class.java]
    }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fav, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            adapter = FavAdapter(requireContext(), ::showDelDialog) {
                val intent = Intent(activity, MainActivity::class.java)
                var name = ""
                try {
                    val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geoCoder.getFromLocation(
                        it.lat,
                        it.lon,
                        1
                    )
                    if (addresses?.size!! > 0) {
                        name = addresses[0].locality
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                intent.putExtra(LATITUDE, it.lat)
                intent.putExtra(LONGITUDE, it.lon)
                intent.putExtra(NAME, name)
                startActivity(intent)
                activity?.finish()
            }
            binding.favAdapter = adapter
            binding.addFab.setOnClickListener { navigateToMapsActivity() }

            lifecycleScope.launch { viewModel.fav.collect(adapter::submitList) }
        }

        private fun showDelDialog(location: Location) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.dialog_del)
                .setPositiveButton(R.string.dialog_del_ok) { dialog, _ ->
                    viewModel.delFav(location)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_del_cancel) { dialog, _ ->
                    // User cancelled the dialog
                    dialog.dismiss()
                }.show()
        }

    private fun navigateToMapsActivity() {
        val intent = Intent(activity, MapActivity::class.java)
        startActivity(intent)
    }
}