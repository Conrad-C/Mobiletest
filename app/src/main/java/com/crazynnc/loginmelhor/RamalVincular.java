package com.crazynnc.loginmelhor;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class RamalVincular extends AppCompatActivity {

    TextView cancelarbutton, solicitarcodigo;
    EditText numeroadd;
    TextInputLayout editNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vincular_ramalmovel);
        getSupportActionBar().hide();
        cancelarbutton = findViewById(R.id.cancelarText);
        solicitarcodigo = findViewById(R.id.autenticatenumber);
        numeroadd = findViewById(R.id.numeroadd);
        editNumber = findViewById(R.id.editemaill);
        cancelarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RamalVincular.this,PosLoginDrawer.class);
                intent.putExtra("frag","Ramal");
                startActivity(intent);
            }
        });
        solicitarcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numeroadd.getText().length()<11){
                    editNumber.setError("Esse numero não é valido");
                }if(numeroadd.getText().length()==0){
                    editNumber.setError("Você precisa preencher esse campo!");
                }if(numeroadd.getText().length()==11){
                    startActivity(new Intent(RamalVincular.this,RamalAutenticar.class));
                }
            }
        });
    }
}
