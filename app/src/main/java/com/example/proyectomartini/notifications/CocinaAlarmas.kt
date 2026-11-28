package com.example.proyectomartini.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.proyectomartini.MainActivity

internal data class CuentaCocina(val fin: Long = 0, val pausa: Long = 0, val terminado: Boolean = false) {
    fun restante() = if (fin > 0) (fin - SystemClock.elapsedRealtime()).coerceAtLeast(0) else pausa
}

internal class CocinaAlarmas(private val context: Context) {
    private val ajustes = context.getSharedPreferences("temporizadores_cocina", Context.MODE_PRIVATE)
    private val alarmas = context.getSystemService(AlarmManager::class.java)

    fun leer(id: String) = CuentaCocina(
        ajustes.getLong("$id/fin", 0),
        ajustes.getLong("$id/pausa", 0),
        ajustes.getBoolean("$id/listo", false)
    )

    private fun guardar(id: String, estado: CuentaCocina) {
        ajustes.edit().putLong("$id/fin", estado.fin).putLong("$id/pausa", estado.pausa)
            .putBoolean("$id/listo", estado.terminado).apply()
    }

    private fun aviso(id: String, titulo: String, fin: Long) = PendingIntent.getBroadcast(
        context, 0, Intent(context, CocinaAlarmaReceiver::class.java)
            .setData(Uri.Builder().scheme("martini").authority("cocina").appendPath(id).build())
            .putExtra("titulo", titulo).putExtra("fin", fin),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun iniciar(id: String, titulo: String, milisegundos: Long) {
        val fin = SystemClock.elapsedRealtime() + milisegundos
        guardar(id, CuentaCocina(fin = fin))
        val aviso = aviso(id, titulo, fin)
        try {
            if (Build.VERSION.SDK_INT < 31 || alarmas.canScheduleExactAlarms()) {
                alarmas.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, fin, aviso)
            } else {
                alarmas.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, fin, aviso)
            }
        } catch (_: SecurityException) {
            alarmas.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, fin, aviso)
        }
    }

    fun pausar(id: String, titulo: String) {
        val estado = leer(id)
        val restante = estado.restante()
        if (restante == 0L) {
            finalizar(id, titulo, estado.fin)
            return
        }
        guardar(id, CuentaCocina(pausa = restante))
        alarmas.cancel(aviso(id, titulo, 0))
    }

    fun reiniciar(id: String, titulo: String) {
        guardar(id, CuentaCocina())
        alarmas.cancel(aviso(id, titulo, 0))
    }

    fun finalizar(id: String, titulo: String, fin: Long) {
        if (fin <= 0 || leer(id).fin != fin || fin > SystemClock.elapsedRealtime()) return
        guardar(id, CuentaCocina(terminado = true))
        val destino = "receta/" + id.substringBefore('/')
        BandejaMartini(context).agregar("Tiempo cumplido", titulo, destino)
        val notificaciones = context.getSystemService(NotificationManager::class.java)
        if (!notificaciones.areNotificationsEnabled()) return
        val canal = "cocina_temporizador"
        notificaciones.createNotificationChannel(NotificationChannel(canal, "Temporizador de cocina", NotificationManager.IMPORTANCE_HIGH))
        val abrir = PendingIntent.getActivity(context, 0,
            Intent(context, MainActivity::class.java)
                .setData(Uri.Builder().scheme("martini").authority("aviso").appendPath(id).build())
                .putExtra("destino_aviso", destino)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE)
        val aviso = NotificationCompat.Builder(context, canal)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Tiempo cumplido")
            .setContentText(titulo)
            .setContentIntent(abrir)
            .setAutoCancel(true)
            .build()
        try { notificaciones.notify(id, 10, aviso) } catch (_: SecurityException) { }
    }
}

class CocinaAlarmaReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.data?.lastPathSegment ?: return
        CocinaAlarmas(context).finalizar(id, intent.getStringExtra("titulo").orEmpty(), intent.getLongExtra("fin", 0))
    }
}
