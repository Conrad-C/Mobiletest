package com.crazynnc.agoravai;

import com.crazynnc.agoravai.MapaAtividade;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String NOME_TEXTO = "com.crazynnc.agoravai.NOME_TEXTO";
    private EditText nomevalor;
    private EditText emailvalor;
    private Button confirmar;
    DatabaseReference trefRaiz = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ttesteRef = trefRaiz.child("teste");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailvalor = findViewById(R.id.emailEdit);
        nomevalor = findViewById(R.id.nomeEdit);
        confirmar = findViewById(R.id.confirmarButton);
        confirmar.setOnClickListener(this);
    }
    @Override
    protected void onStart(){
    super.onStart();

    ttesteRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String testeReff = dataSnapshot.getValue().toString().trim();
            System.out.println(testeReff);
            if(testeReff.equals("SS")){
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Intent notInt = new Intent(MainActivity.this,NotificationActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,notInt,PendingIntent.FLAG_UPDATE_CURRENT);


                String mensSaiu = "Trancou tudo e nao esqueceu de nada?";
                NotificationCompat.Builder notBuild = new NotificationCompat.Builder(MainActivity.this);
                notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
                notBuild.setContentTitle("Você saiu de casa, isso está certo?");
                notBuild.setContentText(mensSaiu);
                notBuild.setAutoCancel(true);
                notInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notBuild.setContentIntent(pendingIntent);
                notificationManager.notify(0,notBuild.build());

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }
            public void EmailSender() {
                String email = emailvalor.getText().toString().trim();
                String nome = nomevalor.getText().toString().trim();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Please Wait");
                builder.setMessage("Um e-mail de confirmação sera enviado para voce, por favor aguarde.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MapaAtividade();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                EmailSender sm = new EmailSender(email, nome);

                sm.execute();
            }
            public void MapaAtividade() {
            Intent intent = new Intent(this, TelaPosLogin.class);
            String nomeTl = nomevalor.getText().toString().trim();
            intent.putExtra(NOME_TEXTO,nomeTl);
            startActivity(intent);
            }


    @Override
    public void onClick(View v) {
       EmailSender();


    }

}




