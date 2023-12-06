package com.example.productivityapp.presentacion.timer;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificacionProgramada extends BroadcastReceiver {

    private final static String PROGRAMADA_CHANNEL = "Programada";
    private final static int PROGRAMADA_NOTIFICATION_ID = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        showNotification(context,
                "Productivity App",
                "Fin de temporizador");
    }

    public static void scheduleNotification(Context context, long delayInMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificacionProgramada.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,  PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delayInMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + delayInMillis, pendingIntent);
        }
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PROGRAMADA_CHANNEL, "MyChannel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, PROGRAMADA_CHANNEL)
                .setSmallIcon(com.google.android.material.R.drawable.ic_keyboard_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(PROGRAMADA_NOTIFICATION_ID, builder.build());
    }
}
