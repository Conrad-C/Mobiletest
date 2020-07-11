package com.crazynnc.loginmelhor;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import java.util.HashMap;
import java.util.Map;


public class LocationService extends Service {

    LocationListener mLocationListener;
    LocationManager mLocationManager;
    Location mLastLoc;
    String urlfixo ="http://douglas.aws.snep7.com/location?type=change&location=";
    double alatitude, alongitude, LocalFixoLat, LocalFixoLng;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        //Recebido chamado de start service
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //Com as notificações, o serviço fica vivo pra sempre
        Notification notification = new NotificationCompat.Builder(this, "Channel id")
                .setContentIntent(pendingIntent)
                .build();
        //Iniciar serviço background/Killed app
        startForeground(1, notification);
        //Pegar valor do local fixo do usuario
        userRef.child("LocalFixoLat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocalFixoLat = Double.parseDouble(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userRef.child("LocalFixoLng").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LocalFixoLng = Double.parseDouble(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Gerenciador de localização para atualizações em tempo real da mesma
        initializeLocationManager();

        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, mLocationListener);

        } catch (java.lang.SecurityException ex) {


        } catch (IllegalArgumentException ex) {


        }

        return START_STICKY;
    }

    private class LocationListener implements android.location.LocationListener{

        public LocationListener(String gpsProvider) {
            mLastLoc = new Location(gpsProvider);
        }

        @Override
        public void onLocationChanged(Location location) {
            //Quando detectado mudança de local, pegar os valores
            mLastLoc = location;
            alatitude = mLastLoc.getLatitude();
            alongitude = mLastLoc.getLongitude();
            compararLoc();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    private void compararLoc() {
        //Comparar localização e atualizar database com a informação
        float[] results = new float[10];
        Location.distanceBetween(alatitude, alongitude, LocalFixoLat, LocalFixoLng, results);
            if (results[0] > 50) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("Esta em casa", "Nao");
                userRef.updateChildren(updates);
                sendrequest("fora");
            } else {
                Map<String, Object> updates1 = new HashMap<>();
                updates1.put("Esta em casa", "Sim");
                userRef.updateChildren(updates1);
                sendrequest("dentro");
            }
        }


    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    private void createNotificationChannel() {
        //Canal da notificação para android version => O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel("Channel id", "Background Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
    private void sendrequest(String saiu){
        //Enviar request avisando a mudança de localização
        Map<String,Object> data = new HashMap<>();
        data.put("url",urlfixo+saiu+"&email="+FirebaseAuth.getInstance().getCurrentUser().getEmail());
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
