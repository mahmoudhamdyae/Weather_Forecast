package com.mahmoudhamdyae.weatherforecast.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.mahmoudhamdyae.weatherforecast.R

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        var message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        if (message == "" && context != null) {
            message = context.getString(R.string.app_name)
        }
        val alarmTypeId = intent.getIntExtra("EXTRA_TYPE", 0)
        val alarmType = if (alarmTypeId == 0) AlarmType.ALARM else AlarmType.NOTIFICATION
        val channelId = "alarm_id"
        context?.let { ctx ->
            val notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            notificationManager.notify(1, builder.build())
        }
    }
}