package com.example.lab11

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Приемник для обработки действий пользователя в уведомлении.
 */
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        when (intent.action) {
            NotificationHelper.ACTION_SKIP -> {
                // Пользователь решил пропустить перерыв
                notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
                Toast.makeText(context, "Перерыв пропущен", Toast.LENGTH_SHORT).show()
            }
            NotificationHelper.ACTION_SNOOZE -> {
                // Пользователь отложил перерыв на 5 минут
                notificationManager.cancel(NotificationHelper.NOTIFICATION_ID)
                
                NotificationHelper(context).scheduleReminder(5)
                Toast.makeText(context, "Напоминание отложено на 5 минут", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

