package com.crazynnc.loginmelhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
TextInputLayout layoutsenha, layoutemail;
DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //Checar se o usuario ja esta logado

        if(mAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this, PosLoginDrawer.class));
        }

        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        logar = findViewById(R.id.logar);
        layoutsenha = findViewById(R.id.editsenhass);
        layoutemail = findViewById(R.id.editemaill);
        //Pegar estado atual do log in

        mAuth = FirebaseAuth.getInstance();

        editSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editSenha.getText().length() > 0){
                    layoutsenha.setErrorEnabled(false);
                }else {
                    layoutsenha.setErrorEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }});
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editEmail.getText().length() > 0){
                    layoutemail.setErrorEnabled(false);
                }else {
                    layoutemail.setErrorEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }});
        //Definir uma acao ao clicar no botao
        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String valoremail = editEmail.getText().toString();
                String valorsenha = editSenha.getText().toString();

                //Checar se os campos estao preenchidos, se nao, mostrar um erro

                if (valorsenha.length() == 0 && valoremail.length() > 0) {
                    layoutsenha.setPasswordVisibilityToggleEnabled(false);
                    layoutsenha.setError("Voce precisa preencher esse campo!");
                    return;
                }
                if (valoremail.length() == 0 && valorsenha.length() > 0) {
                    layoutsenha.setPasswordVisibilityToggleEnabled(false);
                    layoutemail.setError("Voce precisa preencher esse campo!");
                    return;
                }
                if (valorsenha.length() == 0 && valoremail.length() == 0) {
                    Toast.makeText(MainActivity.this, "Todos os campos estao vazios", Toast.LENGTH_SHORT).show();
                    layoutsenha.setError("Voce precisa preencher esse campo!");
                    layoutemail.setError("Voce precisa preencher esse campo!");
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
                                startActivity(new Intent(MainActivity.this, PosLoginDrawer.class));

                                //Avisando se falhou o login

                            }else{
                                layoutsenha.setError("Senha ou usuario incorreto!");
                                layoutemail.setError("↓");
                            }
                        }
                    });
                }
            }
        });
    }
}
