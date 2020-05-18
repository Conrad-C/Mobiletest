package com.crazynnc.agoravai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TelaPosLogin extends MainActivity {

    private EditText nomevalor;
    private TextView bemvindo;
    private Button mapa;
    private TextView logout;
    double CasaLAT;
    double CasaLNG;
    public String email, nome;
    DatabaseReference mAuthBase;
    FirebaseUser curUser;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        mAuthBase = FirebaseDatabase.getInstance().getReference();
        curUser = FirebaseAuth.getInstance().getCurrentUser();
        email = curUser.getEmail();
        Intent nomeint = getIntent();
        Bundle nomeExtra = nomeint.getExtras();
        if(nomeExtra != null)
        {
            nome = nomeExtra.getString("nome");
        }
        Intent casaLoc = getIntent();
        Bundle doubles = casaLoc.getExtras();
        if  (doubles != null)
            {
                CasaLAT = doubles.getDouble("CasaLAT");
                CasaLNG = doubles.getDouble("CasaLNG");
            }
        Usuario usuario = new Usuario(
                email,
                nome,
                CasaLAT,
                CasaLNG

        );
        mAuthBase.child(curUser.getUid()).setValue(usuario);
        bemvindo = findViewById(R.id.bemVindo);
        mapa = findViewById(R.id.mapa);
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
                startActivity(new Intent(TelaPosLogin.this,MainActivity.class));
            }
        });

    }




    public void LocalMapa(){
        Intent mapaLocal = new Intent(this, MapaAtividade.class);
        startActivity(mapaLocal);

    }

}
