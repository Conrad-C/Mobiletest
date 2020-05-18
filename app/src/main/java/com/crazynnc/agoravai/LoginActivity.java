package com.crazynnc.agoravai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText nomevalor;
    private EditText emailvalor;
    private EditText senhavalor;
    Button btn;
    FirebaseAuth mAuthBase;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuthBase = FirebaseAuth.getInstance();
        emailvalor = findViewById(R.id.username);
        senhavalor = findViewById(R.id.password);
        Button btn = findViewById(R.id.buttonLogar);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuarioAtivo = mAuthBase.getCurrentUser();
                if (usuarioAtivo != null) {
                    Toast.makeText(LoginActivity.this, "Você está logado!", Toast.LENGTH_SHORT).show();
                    Intent logou = new Intent(LoginActivity.this, TelaPosLogin.class);
                    startActivity(logou);
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor faça o login!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String email = emailvalor.getText().toString().trim();
                                       String senha = senhavalor.getText().toString().trim();

                                       if (email.isEmpty()) {
                                           emailvalor.setError("Por favor forneça seu endereço de e-mail!");
                                           emailvalor.requestFocus();
                                       }
                                       if (senha.isEmpty()) {
                                           senhavalor.setError("Você precisa de uma senha!");
                                           senhavalor.requestFocus();
                                       }
                                       if (senha.isEmpty() && email.isEmpty()) {
                                           Toast.makeText(LoginActivity.this, "Todos os campos estão vazios, por favor preencha!", Toast.LENGTH_SHORT)
                                                   .show();
                                       } else if (!senha.isEmpty() && !email.isEmpty()) {
                                           mAuthBase.signInWithEmailAndPassword(email, senha).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                               @Override
                                               public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if (!task.isSuccessful()) {
                                                       Toast.makeText(LoginActivity.this, "Falha no Login, por favor tente novamente", Toast.LENGTH_SHORT).show();
                                                   } else {
                                                       startActivity(new Intent(LoginActivity.this, TelaPosLogin.class));
                                                   }
                                               }
                                           });

                                       } else {
                                           Toast.makeText(LoginActivity.this, "Falha no Login!", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }
        );
    }

        @Override
        protected void onStart(){
        super.onStart();
        mAuthBase.addAuthStateListener(authListener);
        }

    }

