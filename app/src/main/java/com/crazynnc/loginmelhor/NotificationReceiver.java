package com.crazynnc.loginmelhor;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class NotificationReceiver extends BroadcastReceiver {
    //Iniciar variaveis
    String url, type, callid, callednumber;
    String urlfixo ="http://douglas.aws.snep7.com/";
    CharSequence transferencia;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Receber chamado, cancelar notificação.
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if(remoteInput!=null){

            transferencia = remoteInput.getCharSequence(FirebaseMessageService.KEY_TRANSFER);
            mNotificationManager.cancel(0);
        }

        type = intent.getStringExtra("action");
        callednumber = intent.getStringExtra("callednumber");
        callid = intent.getStringExtra("callid");
        //Identificar tipo de ação clicada na notificação
        if(type.equals("capturar")){

            url = urlfixo+"call?phone="+callednumber+"&callid="+callid;

        }else if(type.equals("transferir")){

            url = urlfixo+"call?forward="+transferencia+"&voicemail=yes&callid="+callid;


        }
        //Enviar request avisando o clique e pedindo resposta.
        Map<String,Object> data = new HashMap<>();
        data.put("url",url);
        FirebaseFunctions.getInstance()
                .getHttpsCallable("oncallfunctions")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }
}
