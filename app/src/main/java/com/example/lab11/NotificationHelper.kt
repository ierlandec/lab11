package com.example.lab11

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "break_reminder_channel"
        const val NOTIFICATION_ID = 1
        const val ACTION_SKIP = "action_skip"
        const val ACTION_SNOOZE = "action_snooze"

        private const val REQUEST_CODE_SKIP = 100
        private const val REQUEST_CODE_SNOOZE = 101
        private const val REQUEST_CODE_NOTIFICATION = 102
        private const val REQUEST_CODE_ALARM = 103
    }

    fun showBreakReminder() {
        // Intent для открытия приложения при клике на уведомление
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 
            REQUEST_CODE_NOTIFICATION, 
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Intent для действия "Пропустить"
        val skipIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_SKIP
        }
        val skipPendingIntent = PendingIntent.getBroadcast(
            context, 
            REQUEST_CODE_SKIP, 
            skipIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Intent для действия "Отложить"
        val snoozeIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = ACTION_SNOOZE
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context, 
            REQUEST_CODE_SNOOZE, 
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Создание уведомления с помощью NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_break)
            .setContentTitle("Время перерыва!")
            .setContentText("Вы работали достаточно. Пора отдохнуть 5 минут.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_skip, "Пропустить", skipPendingIntent)
            .addAction(R.drawable.ic_snooze, "Отложить", snoozePendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun scheduleReminder(delayMinutes: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 
            REQUEST_CODE_ALARM, 
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTime = System.currentTimeMillis() + delayMinutes * 60 * 1000

        // Используем разные методы в зависимости от версии Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android 6.0+: setExactAndAllowWhileIdle обходит ограничения Doze Mode
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }
}

