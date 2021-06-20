package com.example.hackatonapp.presentation.screen.settings

import android.R
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.*


/**
 * Created by Jaison on 20/06/17.
 */
object NotificationScheduler {
    const val DAILY_REMINDER_REQUEST_CODE = 100
    const val TAG = "NotificationScheduler"
    fun setReminder(context: Context, cls: Class<*>?, hour: Int, min: Int) {
        val calendar = Calendar.getInstance()
        val setcalendar = Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0

        // cancel already scheduled reminders
        cancelReminder(context, cls)
        if (setcalendar.before(calendar)) setcalendar.add(Calendar.DATE, 1)

        // Enable a receiver
        val receiver = ComponentName(context, cls!!)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REMINDER_REQUEST_CODE,
            intent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            setcalendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReminder(context: Context, cls: Class<*>?) {
        // Disable a receiver
        val receiver = ComponentName(context, cls!!)
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
        val intent1 = Intent(context, cls)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DAILY_REMINDER_REQUEST_CODE,
            intent1,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    fun showNotification(context: Context, cls: Class<*>?, title: String?, content: String?) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "3", "My channel default priority",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        val myIntent = Intent(context, AlarmReceiver::class.java)
        myIntent.putExtra("mode", "standard")

        val builder = NotificationCompat.Builder(
            context, "3"
        )
            .setContentTitle("Напоминание")
            .setContentText("Пора проверить давление и записать данные!")
            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        val notification: Notification = builder.build()
        notificationManager.notify(1, notification)
    }
}