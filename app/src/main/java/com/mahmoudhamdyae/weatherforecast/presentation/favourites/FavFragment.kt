package com.mahmoudhamdyae.weatherforecast.presentation.favourites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mahmoudhamdyae.weatherforecast.R
import com.mahmoudhamdyae.weatherforecast.databinding.FragmentFavBinding
import com.mahmoudhamdyae.weatherforecast.domain.model.Location
import com.mahmoudhamdyae.weatherforecast.presentation.map.MapActivity


class FavFragment : Fragment() {

    private lateinit var binding: FragmentFavBinding

    private lateinit var adapter: FavAdapter
    private lateinit var viewModel: FavViewModel

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fav, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            viewModel = ViewModelProvider(requireActivity())[FavViewModel::class.java]
            binding.viewModel = viewModel
            binding.lifecycleOwner = this
            adapter = FavAdapter(::showDelDialog)
            binding.favAdapter = adapter

            viewModel.fav.observe(viewLifecycleOwner, adapter::submitList)

            binding.addFab.setOnClickListener {
                navigateToMapsActivity()
            }
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