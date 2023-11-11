package com.mahmoudhamdyae.weatherforecast.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
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
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val builder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)

            notificationManager.notify(1, builder.build())
        }
    }

    private fun playAudio(context: Context?) {
        val mMediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        mMediaPlayer!!.isLooping = true
        mMediaPlayer.start()
    }
}