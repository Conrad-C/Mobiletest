package com.crazynnc.agoravai;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.ls.LSException;

import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.internet.MimeMessage;


public class MapaAtividade extends AppCompatActivity {
    Location localizacao;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    FirebaseAuth mAuthBase;
    FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth.AuthStateListener authlistener;
    DatabaseReference locData = FirebaseDatabase.getInstance().getReference();

    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Nice1");

        super.onCreate(savedInstanceState);
        DatabaseReference locChild = locData.child("UsuariosCasas");
        String idl = locChild.push().getKey();


        System.out.println("Nice2");

        setContentView(R.layout.mapa);


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);

        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(MapaAtividade.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            System.out.println("Nice4");
            getLocation();
        }else {
            ActivityCompat.requestPermissions(MapaAtividade.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            getLocation();
        }

    }




    private void getLocation() {
        System.out.println("Nice5");
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                System.out.println(location);
                if (location != null) {
                    System.out.println("Nice7");
                    localizacao = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.mapa);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(final GoogleMap googleMap) {
                            LatLng latLng = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
                            final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Definir sua casa aqui?");
                            googleMap.setMyLocationEnabled(true);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                            googleMap.addMarker(markerOptions);
                            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng point) {
                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("Definir sua casa aqui?");
                                    googleMap.clear();
                                    googleMap.addMarker(marker);
                                }
                            });
                            final AlertDialog.Builder confirmLoc = new AlertDialog.Builder(MapaAtividade.this);
                            confirmLoc.setTitle("Confirmar Localizacao");
                            confirmLoc.setMessage("Voce deseja definir essa localizacao como sua casa?");
                            confirmLoc.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast toast1 = Toast.makeText(MapaAtividade.this, "Localizacao definida como casa!", Toast.LENGTH_SHORT);
                                    toast1.show();
                                    if(curUser != null){
                                        double CasaLNG = googleMap.getMyLocation().getLongitude();
                                        double CasaLAT = googleMap.getMyLocation().getLatitude();
                                        Intent casaLoc = new Intent(MapaAtividade.this,TelaPosLogin.class);
                                        Bundle doubles = new Bundle();
                                        doubles.putDouble("CasaLAT",CasaLAT);
                                        doubles.putDouble("CasaLNG",CasaLNG);
                                        casaLoc.putExtras(doubles);
                                        startActivity(casaLoc);
                                }}
                            });
                            confirmLoc.setNegativeButton("NAO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast toast = Toast.makeText(MapaAtividade.this, "Voce pode definir sua casa a qualquer momento!", Toast.LENGTH_SHORT);
                                    toast.show();
                                    Intent naoConfirmar = new Intent(MapaAtividade.this, TelaPosLogin.class);
                                    startActivity(naoConfirmar);
                                }
                            });
                            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    confirmLoc.show();
                                    return false;
                                }
                            });

                        }

                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44)
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }

        }




}
