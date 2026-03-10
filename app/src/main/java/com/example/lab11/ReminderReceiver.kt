package com.example.lab11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Приемник, который срабатывает при наступлении времени перерыва.
 */
class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // При получении сигнала от AlarmManager показываем уведомление
        val helper = NotificationHelper(context)
        helper.showBreakReminder()
    }
}

