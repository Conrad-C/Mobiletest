package com.crazynnc.loginmelhor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class RamalAutenticar extends AppCompatActivity {
    Button autenticarsms;
    EditText SMS;
    TextView cancelarbutton;
    TextInputLayout smslayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autenticar_codigo_ramalmovel);
        getSupportActionBar().hide();
        autenticarsms = findViewById(R.id.autenticatenumber);
        cancelarbutton = findViewById(R.id.cancelarbutton);
        smslayout = findViewById(R.id.editemaill);
        SMS = findViewById(R.id.numeroadd);
        autenticarsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SMS.getText().length()<8){
                    smslayout.setError("Oops, acho que você esqueceu de algo!");
                }if(SMS.getText().length()==0){
                    smslayout.setError("Você precisa preencher esse campo!");
                }if(SMS.getText().length()==8){
                    startActivity(new Intent(RamalAutenticar.this,RamalAndamento.class));
                }
            }
        });
        cancelarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RamalAutenticar.this,PosLoginDrawer.class);
                intent.putExtra("frag","Ramal");
                startActivity(intent);
            }
        });

    }
}
