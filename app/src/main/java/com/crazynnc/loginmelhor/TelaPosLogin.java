package com.crazynnc.loginmelhor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class TelaPosLogin extends AppCompatActivity {

    Button definir;
    TextView logout;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poslogin);
        getSupportActionBar().hide();
        definir = findViewById(R.id.button);

        //Garantindo a validez da sessao para pegar o token unico do dispositivo

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String idToken = instanceIdResult.getToken();

                //Adicionando o token ao banco de dados

                Map<String, Object> updates = new HashMap<>();
                updates.put("UniqueToken",idToken);
                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(updates);
            }
        });

        definir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(TelaPosLogin.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(TelaPosLogin.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("boa1","if chegou");
                    ActivityCompat.requestPermissions(TelaPosLogin.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }else{
                    startActivity(new Intent(TelaPosLogin.this,MapActivity.class));
                }
            }

        });
    }

}
