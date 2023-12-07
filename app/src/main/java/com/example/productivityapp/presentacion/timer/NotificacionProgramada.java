package com.example.productivityapp.presentacion.timer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.productivityapp.presentacion.MainActivity;

public class NotificacionProgramada extends BroadcastReceiver {
    public final static int PROGRAMADA_NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.crearNotificacionRepetible(context);
    }

    @SuppressLint("MissingPermission")
    private void crearNotificacionRepetible(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        );

        Notification notificacion = new NotificationCompat.Builder(context, TimerFragment.SIMPLE_CHANNEL)
                .setSmallIcon(com.google.android.material.R.drawable.ic_keyboard_black_24dp)
                .setContentTitle("Notificación programada")
                .setContentText("Esto es el cuerpo.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notificacion);
    }
}
