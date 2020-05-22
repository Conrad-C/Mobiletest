package com.crazynnc.agoravai;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service implements LocationListener {
    private static final String CHANNEL_NAME = "SAIR, ENTRAR EM CASA";
    private static final String CHANNEL_DESCRIPTION = "Notificacoes AgoraVai";
    private static final String CHANNEL_ID = "canal";
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    int raioCircle = 15;
    LocationManager locationManager;
    Location location;
    Double CasaLAT,CasaLNG,AtualLat,AtualLng;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    public static String str_receiver = "servicetutorial.service.receiver";
    DatabaseReference refNomeEmail = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(),5,notify_interval);
        intent = new Intent(str_receiver);
        refNomeEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String LatString = dataSnapshot.child("Latitude").getValue().toString();
                String ALatString = dataSnapshot.child("AtualLat").getValue().toString();
                String LngString = dataSnapshot.child("Longitude").getValue().toString();
                String ALngString = dataSnapshot.child("AtualLng").getValue().toString();
                CasaLAT = Double.parseDouble(LatString);
                CasaLNG = Double.parseDouble(LngString);
                AtualLat = Double.parseDouble(ALatString);
                AtualLng = Double.parseDouble(ALngString);
                float distance[] = new float[10];
                Location.distanceBetween(AtualLat,AtualLng,CasaLAT,CasaLNG,distance);
                if(distance[0]<raioCircle){
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("Esta em casa?", "Sim");
                    refNomeEmail.updateChildren(updates);
                    String message = "Voce chegou em casa!";
                    String titulo = "Descanse bem!";
                    new NotificationHelper(getApplicationContext()).notify(message, titulo);
                    notification(message, titulo);

                }else if(distance[0]>raioCircle){
                    Map<String, Object> updates = new HashMap<String, Object>();
                    updates.put("Esta em casa?", "Nao");
                    refNomeEmail.updateChildren(updates);
                    String message = "Voce saiu de casa!";
                    String titulo = "Divirta-se!";
                    new NotificationHelper(getApplicationContext()).notify(message, titulo);
                    notification(message, titulo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {

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

    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location!=null){

                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable){
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,this);
                if (locationManager!=null){
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location!=null){
                        Log.e("latitude",location.getLatitude()+"");
                        Log.e("longitude",location.getLongitude()+"");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Map<String, Object> updates = new HashMap<String, Object>();
                        updates.put("AtualLat",latitude);
                        updates.put("AtualLng",longitude);
                        refNomeEmail.updateChildren(updates);
                        fn_update(location);
                    }
                }
            }


        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location){

        intent = new Intent();
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);

        sendBroadcast(intent);
    }
    public void notification(String message, String titulo) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notBuild = new NotificationCompat.Builder(getApplicationContext());
        notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
        notBuild.setContentTitle(titulo);
        notBuild.setContentText(message);
        notBuild.setAutoCancel(true);
        notificationManager.notify(0, notBuild.build());
    }
    private void createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.canShowBadge();
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setLightColor(getResources().getColor(R.color.colorAccent));
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


}