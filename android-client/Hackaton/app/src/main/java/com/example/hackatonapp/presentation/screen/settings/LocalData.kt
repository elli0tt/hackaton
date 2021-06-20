package com.example.hackatonapp.presentation.screen.settings

import android.content.Context
import android.content.SharedPreferences


class LocalData(context: Context) {
    private val appSharedPrefs: SharedPreferences =
        context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)
    private val prefsEditor: SharedPreferences.Editor = appSharedPrefs.edit()

    // Settings Page Set Reminder
    var reminderStatus: Boolean
        get() = appSharedPrefs.getBoolean(Companion.reminderStatus, false)
        set(status) {
            prefsEditor.putBoolean(Companion.reminderStatus, status)
            prefsEditor.commit()
        }

    var textStatus: String
        get() = appSharedPrefs.getString(Companion.textStatus, "Вы ничего не выбрали")!!
        set(value) {
            prefsEditor.putString(Companion.textStatus, value)
            prefsEditor.commit()
        }

    // Settings Page Reminder Time (Hour)
    fun get_hour(): Int {
        return appSharedPrefs.getInt(hour, 20)
    }

    fun set_hour(h: Int) {
        prefsEditor.putInt(hour, h)
        prefsEditor.commit()
    }

    // Settings Page Reminder Time (Minutes)
    fun get_min(): Int {
        return appSharedPrefs.getInt(min, 0)
    }

    fun set_min(m: Int) {
        prefsEditor.putInt(min, m)
        prefsEditor.commit()
    }

    fun reset() {
        prefsEditor.clear()
        prefsEditor.commit()
    }

    companion object {
        private const val APP_SHARED_PREFS = "RemindMePref"
        private const val reminderStatus = "reminderStatus"
        private const val hour = "hour"
        private const val min = "min"
        private const val textStatus = "textStatus"
    }

}