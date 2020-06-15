package com.crazynnc.agoravai;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class NotificationActivity extends BroadcastReceiver{
    Double latitude,longitude;
    DatabaseReference refNomeEmail = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    public void onReceive(Context context, Intent intent) {
    latitude = Double.parseDouble(intent.getStringExtra("latitude"));
    longitude = Double.parseDouble(intent.getStringExtra("latitude"));
    Map<String, Object> updates = new HashMap<String,Object>();
    updates.put("AtualLat",latitude);
    updates.put("AtualLng",longitude);
    refNomeEmail.updateChildren(updates);
    }

}



