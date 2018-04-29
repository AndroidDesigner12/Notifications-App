package com.example.william.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;


public class Alarm extends BroadcastReceiver {

    private Context context;
    private NotificationManager manager;
    private  NotificationCompat.Builder builder;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel("channel", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GRAY);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(notificationChannel);

        }else {

        }

        String title = "BC App";
        String text = "Your Class in Akshara is about to Start";

        Intent intents = new Intent(context,Image.class);

        NotificationCompat.Builder builder = initNotificBuilder(title,text,intents);
        getManager().notify(1,builder.build());

/*
        MediaPlayer mediaPlayer = MediaPlayer.create(context, android.provider.Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
*/


    }


    private NotificationCompat.Builder initNotificBuilder(String title, String text, Intent intent) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            builder = new NotificationCompat.Builder(context,"channel")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setVibrate(new long[]{500,1000,1000,1000})
                    .setSound(alarmSound)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setContentText(text);


        if (intent != null){
            PendingIntent pendingIntent =  PendingIntent.getActivity(context,0,intent,0);
            builder.setContentIntent(pendingIntent);
        }

        return builder;
    }

    public NotificationManager getManager() {

        if (manager == null)
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;

    }
}
