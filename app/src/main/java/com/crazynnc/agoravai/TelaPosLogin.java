package com.crazynnc.agoravai;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class TelaPosLogin extends MainActivity {

    LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
    private static final long MIN_TIME_BW_UPDATES = 1;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 30;
    private TextView bemvindo;
    private Button mapa;
    private TextView logout;
    double CasaLAT, AtualLAT;
    double CasaLNG, AtualLNG;
    double raioCircle;
    public String email, nome;
    DatabaseReference mAuthBase;
    FirebaseUser curUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        mAuthBase = FirebaseDatabase.getInstance().getReference();
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        email = curUser.getEmail();
        bemvindo = findViewById(R.id.bemVindo);
        mapa = findViewById(R.id.mapa);
        Bundle doubles = getIntent().getExtras();
        if (doubles != null) {
            AtualLAT = doubles.getDouble("AtualLAT");
            CasaLAT = doubles.getDouble("CasaLAT");
            AtualLNG = doubles.getDouble("AtualLNG");
            CasaLNG = doubles.getDouble("CasaLNG");
            raioCircle = doubles.getDouble("raioCircle");
        }
        float[] results = new float[1];
        Location.distanceBetween(AtualLAT,AtualLNG,CasaLAT,CasaLNG,results);
        if(results[0] < raioCircle){
            System.out.println("Entrou em casa");
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            String mensSaiu = "Descanse bem!";
            NotificationCompat.Builder notBuild = new NotificationCompat.Builder(TelaPosLogin.this);
            notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
            notBuild.setContentTitle("Você chegou em casa!");
            notBuild.setContentText(mensSaiu);
            notBuild.setAutoCancel(true);
            notificationManager.notify(0,notBuild.build());
        }else if(results[0] > raioCircle){
            System.out.println("Saiu de casa");
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            String mensSaiu = "Se divirta!";
            NotificationCompat.Builder notBuild = new NotificationCompat.Builder(TelaPosLogin.this);
            notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
            notBuild.setContentTitle("Você saiu de casa!");
            notBuild.setContentText(mensSaiu);
            notBuild.setAutoCancel(true);
            notificationManager.notify(0,notBuild.build());
        }
        Location latLng = new Location(Location.convert(CasaLAT+CasaLNG,1));


        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalMapa();
            }
        });
        bemvindo.setText("Bem vindo!");
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout.setTextColor(Color.BLACK);
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(TelaPosLogin.this, MainActivity.class));
            }
        });
    }

    public void LocalMapa(){
        Intent mapaLocal = new Intent(this, MapaAtividade.class);
        startActivity(mapaLocal);

    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            float[] results = new float[1];
            Location.distanceBetween(AtualLAT,AtualLNG,CasaLAT,CasaLNG,results);
            if(results[0] < raioCircle){
                System.out.println("Entrou em casa");
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                String mensSaiu = "Descanse bem!";
                NotificationCompat.Builder notBuild = new NotificationCompat.Builder(TelaPosLogin.this);
                notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
                notBuild.setContentTitle("Você chegou em casa!");
                notBuild.setContentText(mensSaiu);
                notBuild.setAutoCancel(true);
                notificationManager.notify(0,notBuild.build());
            }else if(results[0] > raioCircle){
                System.out.println("Saiu de casa");
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                String mensSaiu = "Se divirta!";
                NotificationCompat.Builder notBuild = new NotificationCompat.Builder(TelaPosLogin.this);
                notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
                notBuild.setContentTitle("Você saiu de casa!");
                notBuild.setContentText(mensSaiu);
                notBuild.setAutoCancel(true);
                notificationManager.notify(0,notBuild.build());
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

}