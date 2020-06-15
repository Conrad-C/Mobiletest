package com.crazynnc.agoravai;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

public class ChamadaReceiver extends BroadcastReceiver {
    String numero;
    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            String estado = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(estado == null){
                numero=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            }
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            }
            if (estado.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            }
            if (estado.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                Intent i = new Intent(context, AlertActivity.class);
                i.putExtra("numero", numero);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
