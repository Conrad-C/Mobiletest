package com.crazynnc.loginmelhor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RamalAndamento extends AppCompatActivity {
    TextView cancelarbutton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vinculo_andamento);
        getSupportActionBar().hide();
        cancelarbutton = findViewById(R.id.textView7);
        cancelarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RamalAndamento.this,PosLoginDrawer.class);
                intent.putExtra("frag","Ramal");
                startActivity(intent);
            }
        });
    }
}
