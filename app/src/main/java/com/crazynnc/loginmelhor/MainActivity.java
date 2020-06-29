package com.crazynnc.loginmelhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

private FirebaseAuth mAuth;
Button logar;
EditText editSenha,editEmail;
DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checar se o usuario ja esta logado

        if(mAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, TelaPosLogin.class));
        }

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        logar = findViewById(R.id.logar);

        //Pegar estado atual do log in

        mAuth = FirebaseAuth.getInstance();


        //Definir uma acao ao clicar no botao
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String valoremail = editEmail.getText().toString();
                String valorsenha = editSenha.getText().toString();

                //Checar se os campos estao preenchidos, se nao, mostrar um erro

                if (valorsenha.isEmpty()) {
                    editSenha.setError("Voce precisa preencher esse campo!");
                    return;
                }
                if (valoremail.isEmpty()) {
                    editEmail.setError("Voce precisa preencher esse campo!");
                    return;
                }
                if (valorsenha.isEmpty() && valoremail.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Todos os campos estao vazios", Toast.LENGTH_SHORT).show();
                } else {

                    //Se os campos estiverem preenchidos, checar se o usuario existe

                    mAuth.signInWithEmailAndPassword(valoremail, valorsenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(MainActivity.this, "Sucesso no Login!", Toast.LENGTH_SHORT).show();

                                //Adicionando email ao banco de dados

                                Map<String, Object> updates = new HashMap<>();
                                updates.put("Email",valoremail);
                                ref.child(mAuth.getCurrentUser().getUid()).updateChildren(updates);
                                startActivity(new Intent(MainActivity.this, TelaPosLogin.class));

                                //Avisando se falhou o login

                            }else{
                                Toast.makeText(MainActivity.this, "Falha "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
