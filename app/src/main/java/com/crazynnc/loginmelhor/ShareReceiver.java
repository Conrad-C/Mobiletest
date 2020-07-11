package com.crazynnc.loginmelhor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShareReceiver extends BroadcastReceiver {
    String shareobject;
    String callernumber, calldate, duration, audiourl, callednumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Receber chamado e compartilhar informações da chamada
        callernumber = intent.getStringExtra("callernumber");
        calldate = intent.getStringExtra("calldate");
        duration = intent.getStringExtra("duration");
        audiourl = intent.getStringExtra("audiourl");
        callednumber = intent.getStringExtra("callednumber");
        shareobject = "Chamada de "+callernumber+".\n" +
                "Para "+callednumber+" no dia "+calldate+".\n"+
                "Durou "+duration+" segundos\n"+
                "Link para audio da chamada "+audiourl;

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareobject);
        shareIntent.setType("text/plain");
        Intent sendIntent = Intent.createChooser(shareIntent, null);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(sendIntent);
    }
}
