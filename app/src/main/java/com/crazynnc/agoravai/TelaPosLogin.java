package com.crazynnc.agoravai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPosLogin extends MainActivity {

    private EditText nomevalor;
    private TextView bemvindo;
    private Button mapa;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        Intent intent = getIntent();
        String nomeTl = intent.getStringExtra(MainActivity.NOME_TEXTO);
        bemvindo = findViewById(R.id.bemVindo);
        mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalMapa();
            }
        });
        bemvindo.setText("Bem vindo "+ nomeTl+"!");

    }


    public void LocalMapa(){
        Intent mapaLocal = new Intent(this, MapaAtividade.class);
        startActivity(mapaLocal);

    }
}
