package com.crazynnc.agoravai;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_NAME = "SAIR, ENTRAR EM CASA";
    private static final String CHANNEL_DESCRIPTION = "Notificacoes AgoraVai";
    private static final String CHANNEL_ID = "canal";
    private static NotificationManager notificationManager;
    private Context base;


    public NotificationHelper(Context base) {
        super(base);

        this.base = base;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }



    private void createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.canShowBadge();
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setLightColor(getResources().getColor(R.color.colorAccent));

            getNotificationManager().createNotificationChannel(notificationChannel);
        }
    }

    public NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) base.getSystemService(Context.NOTIFICATION_SERVICE);
        }else{
            notificationManager = (NotificationManager) base.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    return notificationManager;
    }


    public void notify(String message, String titulo) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(base, CHANNEL_ID);
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder.setContentTitle(titulo);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentText(message);
        builder.setSound(notificationUri);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        getNotificationManager().notify(9001, builder.build());
    }

}