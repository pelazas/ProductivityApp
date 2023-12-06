package com.example.productivityapp.presentacion.timer

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class ProgramadaNotification:BroadcastReceiver() {

    companion object {
        const val PROGRAMADA_CHANNEL = "Programada"
        const val PROGRAMADA_NOTIFICATION_ID = 2
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        crearNotificacionRepetible(context!!)
    }

    private fun crearNotificacionRepetible(context: Context) {
        val intent = Intent(context, TimerFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificacion = NotificationCompat.Builder(context,TimerFragment.SIMPLE_CHANNEL)
            .setSmallIcon(com.google.android.material.R.drawable.ic_keyboard_black_24dp)
            .setContentTitle("Notificación programada")
            .setContentText("Esto es el cuerpo.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(PROGRAMADA_NOTIFICATION_ID,notificacion)
    }

}