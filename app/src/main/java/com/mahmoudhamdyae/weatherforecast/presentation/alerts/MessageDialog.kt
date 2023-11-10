package com.mahmoudhamdyae.weatherforecast.presentation.alerts

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mahmoudhamdyae.weatherforecast.R

class MessageDialog(private val onPositiveButtonClicked: () -> Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            builder.setView(inflater.inflate(R.layout.dialog_add_message, null))
                // Add action buttons.
                .setPositiveButton(R.string.label_dialog_ok) { dialog, _ ->
                    dialog?.cancel()
                    onPositiveButtonClicked
                }
                .setNegativeButton(R.string.label_dialog_cancel) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}