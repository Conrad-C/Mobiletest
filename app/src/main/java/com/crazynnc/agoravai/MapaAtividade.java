package com.crazynnc.agoravai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.ls.LSException;

import javax.mail.internet.MimeMessage;


public class MapaAtividade extends AppCompatActivity {
    Location localizacao;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Nice1");

        super.onCreate(savedInstanceState);
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
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(localizacao.getLatitude(),localizacao.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Voce esta aqui!");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));
                            googleMap.addMarker(markerOptions);
            }
        });


                }

            }
        } );{

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44)
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }

        }

}
