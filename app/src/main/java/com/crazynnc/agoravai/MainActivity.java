package com.crazynnc.agoravai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String NOME_TEXTO = "com.crazynnc.agoravai.NOME_TEXTO";
    private EditText nomevalor;
    private EditText emailvalor;
    private EditText senhavalor;
    double CasaLAT, CasaLNG;
    private int senhaL;
    DatabaseReference trefRaiz = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ttesteRef = trefRaiz.child("Nomes");
    FirebaseAuth mAuthBase = FirebaseAuth.getInstance();
    TextView jatemuma;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nomevalor = findViewById(R.id.nomeEdit);
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usuarioativo = mAuthBase.getCurrentUser();
                String nome = nomevalor.getText().toString().trim();
                if(usuarioativo!=null){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();
                    usuarioativo.updateProfile(profileUpdates);
                    startActivity(new Intent(MainActivity.this,TelaPosLogin.class));
                }
            }
        };
        jatemuma = findViewById(R.id.textView6);
        emailvalor = findViewById(R.id.emailEdit);
        senhavalor = findViewById(R.id.senhaEdit);
        Button confirmar = findViewById(R.id.confirmarButton);
        jatemuma.setText("Ja tem uma conta?Entre por aqui!");
        jatemuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
       });
        confirmar.setOnClickListener(this);
        }
    @Override
    protected void onStart(){
    super.onStart();

    /*ttesteRef.addValueEventListener(new ValueEventListener() {
        @Override
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String testeReff = dataSnapshot.getValue().toString().trim();
            System.out.println(testeReff);
            if(testeReff.equals("SS")){
                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Intent notInt = new Intent(MainActivity.this,NotificationActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,notInt,PendingIntent.FLAG_UPDATE_CURRENT);


                String mensSaiu = "Trancou tudo e nao esqueceu de nada?";
                NotificationCompat.Builder notBuild = new NotificationCompat.Builder(MainActivity.this);
                notBuild.setSmallIcon(R.drawable.ic_5efd303cdfc177c656973b2ec7b0d8ee_google_maps_png_transparent_google_mapspng_images_pluspng_1600_1600);
                notBuild.setContentTitle("Você saiu de casa, isso está certo?");
                notBuild.setContentText(mensSaiu);
                notBuild.setAutoCancel(true);
                notInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                notBuild.setContentIntent(pendingIntent);
                notificationManager.notify(0,notBuild.build());

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });*/

    }
            public void EmailSender() {
                String email = emailvalor.getText().toString().trim();
                final String nome = nomevalor.getText().toString().trim();
                String senha = senhavalor.getText().toString().trim();
                senhaL = senha.length();
                if (email.isEmpty()){
                    emailvalor.setError("Por favor forneça seu endereço de e-mail!");
                    emailvalor.requestFocus();
                } else if(nome.isEmpty()){
                    nomevalor.setError("Seu nome é de extrema importancia!");
                    nomevalor.requestFocus();
                } else if(senha.isEmpty()){
                    senhavalor.setError("Você precisa de uma senha!");
                    senhavalor.requestFocus();
                } else if(senhaL <= 5) {
                    senhavalor.setError("A senha deve conter mais de 5 caracteres!");
                    senhavalor.requestFocus();
                } else if(!email.contains("@")){
                    emailvalor.setError("Voce deve inserir um email valido!");
                    emailvalor.requestFocus();
                }if(senha.isEmpty() && email.isEmpty() && nome.isEmpty()){
                        Toast.makeText(MainActivity.this,"Todos os campos estão vazios, por favor preencha!",Toast.LENGTH_SHORT)
                                .show();
                    }else if(!senha.isEmpty() && !email.isEmpty() && !nome.isEmpty() && email.contains("@") && senhaL>5){
                        System.out.println("A");
                        mAuthBase.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Falha no registro, por favor tente novamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent nomeint = new Intent(MainActivity.this,LoginActivity.class);
                                    Bundle nomeExtra = new Bundle();
                                    nomeExtra.putString("nome",nome);
                                    startActivity(nomeint);

                                }
                            }
                        });

                    }else {
                        Toast.makeText(MainActivity.this, "Falha no registro!", Toast.LENGTH_SHORT).show();
                    }
                }






    @Override
    public void onClick(View v) {
       EmailSender();



    }

}




