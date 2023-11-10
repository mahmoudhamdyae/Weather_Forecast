package com.mahmoudhamdyae.weatherforecast.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.mahmoudhamdyae.weatherforecast.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        Log.i("hahahahaha", "onReceive: $message")
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Alarm Demo")
                .setContentText("Notification sent with message $message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }
    }
}