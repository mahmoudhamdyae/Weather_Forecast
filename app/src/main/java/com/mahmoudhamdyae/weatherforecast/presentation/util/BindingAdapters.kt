package com.mahmoudhamdyae.weatherforecast.presentation.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mahmoudhamdyae.weatherforecast.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@BindingAdapter("time")
fun getTodayTime(view: TextView, time: LocalDateTime?) {
    val formattedTime = time?.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )
    view.text = view.context.getString(R.string.today, formattedTime)
}

@BindingAdapter("formattedTime")
fun getFormattedTime(view: TextView, time: LocalDateTime?) {
    view.text = time?.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    val url = "https://openweathermap.org/img/wn/$imgUrl@2x.png"
    url.let {
        Glide.with(imgView.context)
            .load(it)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

@BindingAdapter("isLoading")
fun bindIsLoading(view: View, isLoading: Boolean) {
    if (!isLoading) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("isErrorNull")
fun bindIsErrorNullability(view: View, error: String?) {
    if (error == null) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("isLoading", "isErrorNull")
fun bindLoadingAndError(view: View, isLoading: Boolean, error: String?) {
    if (error == null && !isLoading) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("errorVisibility")
fun bindErrorVisibility(view: View, error: String?) {
    if (error != null) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("visibilityIfEmpty")
fun bindVisibility(view: View, list: List<Any>) {
    if (list.isEmpty()) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bindTime")
fun bindTime(view: TextView, time: String) {
    val hourAndMinute = time.split(":")
    val hour = if (hourAndMinute[0].toInt() > 12) hourAndMinute[0].toInt() - 12 else hourAndMinute[0].toInt()
    val amOrPm = if (hourAndMinute[0].toInt() > 12) "PM" else "AM"
    val minute = hourAndMinute[1]
    view.text = "$hour:$minute $amOrPm"
}

@SuppressLint("SimpleDateFormat", "SetTextI18n")
@BindingAdapter("bindDate")
fun bindDate(view: TextView, date: String) {
    val dayAndMonth = date.split(" ")
    val day = dayAndMonth[0]
    val month = dayAndMonth[1].toInt()
    val cal = Calendar.getInstance()
    cal.set(Calendar.MONTH, month)
    val monthName: String = SimpleDateFormat("MMMM").format(cal.time)
    view.text = "$day $monthName"
}