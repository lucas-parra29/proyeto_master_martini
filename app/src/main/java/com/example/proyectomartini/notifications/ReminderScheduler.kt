package com.example.proyectomartini.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

object ReminderScheduler {

    fun programar(
        context: Context,
        titulo: String,
        mensaje: String,
        segundos: Long = 10
    ) {
        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).apply {
            putExtra("titulo", titulo)
            putExtra("mensaje", mensaje)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(
            Context.ALARM_SERVICE
        ) as AlarmManager

        val tiempo = SystemClock.elapsedRealtime() +
                segundos.coerceAtLeast(1) * 1000L

        alarmManager.setAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            tiempo,
            pendingIntent
        )
    }
}
