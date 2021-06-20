package com.example.hackatonapp.presentation.screen.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.hackatonapp.presentation.AppNavHostActivity
import com.example.hackatonapp.presentation.screen.data_list.PatientDataListFragment

class AlarmReceiver : BroadcastReceiver() {
    var TAG = "AlarmReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && context != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED")
                val localData = LocalData(context)
                NotificationScheduler.setReminder(
                    context,
                    AlarmReceiver::class.java, localData.get_hour(), localData.get_min()
                )
                return
            }
        }
        Log.d(TAG, "onReceive: ")

        //Trigger the notification
        NotificationScheduler.showNotification(
            context, AppNavHostActivity::class.java,
            "You have 5 unwatched videos", "Watch them now?"
        )
    }
}