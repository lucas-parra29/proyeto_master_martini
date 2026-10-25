
package com.example.proyectomartini.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val manager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val canalId = "recordatorios_martini"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                canalId,
                "Recordatorios Master Martini",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            manager.createNotificationChannel(canal)
        }

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val titulo = intent.getStringExtra("titulo")
            ?: "Master Martini"

        val mensaje = intent.getStringExtra("mensaje")
            ?: "Tienes nuevos contenidos disponibles"

        val notificacion = NotificationCompat.Builder(
            context,
            canalId
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            manager.notify(
                System.currentTimeMillis().toInt(),
                notificacion
            )
        } catch (e: SecurityException) {
            Log.e(
                "ReminderReceiver",
                "No se pudo mostrar la notificación",
                e
            )
        }
    }
}
