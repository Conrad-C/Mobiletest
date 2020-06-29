package com.crazynnc.loginmelhor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class FirebaseMessageService extends FirebaseMessagingService {
    String type, callernumber, calldate, duration, audiourl, callednumber, callid;
    public static final String KEY_TRANSFER = "NotificationReply";
    //Receber mensagem enviada pelo cloud functions e pegar as informacoes da notificacao

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("TAG","Garantir que chegou");
        Map<String, String> data = remoteMessage.getData();

        type = data.get("type");
        callernumber = data.get("callernumber");
        calldate = data.get("calldate");
        duration = data.get("duration");
        audiourl = data.get("audio");
        callednumber = data.get("to");
        callid = data.get("callid");
        sendNotification();
    }

    //Montar notificacao com as informacoes dadas pelo metodo anterior

    private void sendNotification() {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Channel Id");

        //Para notificacao ser enviada em background

        Intent intent = new Intent(this, TelaPosLogin.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent broadIntentCap = new Intent(this,NotificationReceiver.class);
        //Informacoes extras para o receiver
        broadIntentCap.putExtra("action","capturar");
        broadIntentCap.putExtra("callednumber",callernumber);
        broadIntentCap.putExtra("callid",callid);

        Intent broadIntentMan = new Intent(this,NotificationReceiver.class);
        //Informacoes extras para o receiver
        broadIntentMan.putExtra("action","transferir");
        broadIntentMan.putExtra("callid",callid);

        Intent broadIntentCom = new Intent(this,ShareReceiver.class);
        //Informacoes extras para o receiver
        broadIntentCom.putExtra("action","share");
        broadIntentCom.putExtra("callernumber",callernumber);
        broadIntentCom.putExtra("calldate",calldate);
        broadIntentCom.putExtra("duration",duration);
        broadIntentCom.putExtra("audiourl",audiourl);
        broadIntentCom.putExtra("callednumber",callednumber);

        PendingIntent pendingActionCap = PendingIntent.getBroadcast(this,0,broadIntentCap,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingActionCom = PendingIntent.getBroadcast(this,0,broadIntentCom,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingActionMan = PendingIntent.getBroadcast(this,0,broadIntentMan,PendingIntent.FLAG_UPDATE_CURRENT);


        //Corpo da notificacao

        String numberemail = "Numerou ou email";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TRANSFER)
                .setLabel(numberemail)
                .build();

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_announcement_black_24dp,"Transferir",pendingActionMan).addRemoteInput(remoteInput).setAllowGeneratedReplies(true).build();

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_announcement_black_24dp);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(bigText);

        if(type.equals("ringing")) {
            Log.d("dd","entrouaq");
            mBuilder.setContentTitle("Chamada em seu ramal");
            mBuilder.setContentText("de "+callernumber);
            mBuilder.addAction(R.drawable.ic_announcement_black_24dp, "Capturar", pendingActionCap);
            mBuilder.addAction(action);

        }else if(type.equals("hangup")){
            Log.d("dd","entrouaq");
            mBuilder.setContentTitle("Chamada se encerrou em seu ramal");
            mBuilder.setContentText("Voce pode compartilhar os dados dessa chamada!");
            mBuilder.addAction(R.drawable.ic_announcement_black_24dp, "Compartilhar", pendingActionCom);

        }
        //Canais sao necessarios do android 8.0 para cima

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("Channel Id", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }
        //Enviar notificacao

        mNotificationManager.notify(0, mBuilder.build());



    }
}
