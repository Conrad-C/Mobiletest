package com.crazynnc.agoravai;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class AlertActivity extends Activity {
    DatabaseReference refNomeEmail = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String number = getIntent().getStringExtra("numero");
        System.out.println(number);

        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Chamada encerrada");
        dialog.setMessage("Voce deseja salvar os dados dessa chamada?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(AlertActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Cursor managedCursor = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                Calendar calendar = Calendar.getInstance();
                String date = DateFormat.getDateInstance().format(calendar.getTime());
                Map<String, Object> updates = new HashMap<String,Object>();
                updates.put("Numero",number);
                updates.put("Data",date);
                refNomeEmail.child("Historico de Chamadas").child(number+", "+date).updateChildren(updates);
                startActivity(new Intent(AlertActivity.this,TelaPosLogin.class));
            }
        });
        dialog.setNegativeButton("NAO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlertActivity.this,"Voce podera salvar todas as chamadas!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AlertActivity.this,TelaPosLogin.class));
            }
        });
        AlertDialog create = dialog.create();
        create.show();

    }
}
