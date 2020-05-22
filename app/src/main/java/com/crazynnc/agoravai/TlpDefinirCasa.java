package com.crazynnc.agoravai;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TlpDefinirCasa extends Fragment {
    private static final int REQUEST_CODE = 44;
    DatabaseReference refNomeEmail = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private Button mapa;
    NotificationManager notificationManager;
    public int count;
    double CasaLAT;
    double CasaLNG;
    double raioCircle;
    private static final String CHANNEL_NAME = "SAIR, ENTRAR EM CASA";
    private static final String CHANNEL_ID = "Canal";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View v = inflater.inflate(R.layout.fragment_tela_p_l_atividade, container, false);
        raioCircle = 15;
        mapa = v.findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalMapa();
            }
        });
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE
            );
        }
        return v;
    }

    private void LocalMapa() {
        count++;
        Intent mapaLocal = new Intent(getActivity(), MapaAtividade.class);
        startActivity(mapaLocal);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

}
