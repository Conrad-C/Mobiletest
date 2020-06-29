package com.crazynnc.loginmelhor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    Location curLoc;
    double LocalFixoLat, LocalFixoLng;
    FusedLocationProviderClient flpc;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);

        //Pegar o gps localizador do celular do usuario/verificar existencia
        flpc = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    private void fetchLastLocation() {

        //Pegar uma ultima localizacao registrada
        Task<Location> task = flpc.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //Verificar se o localizacao nao é nula e definir mapa para abrir
                if (location != null) {
                    curLoc = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MapActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Criar uma localização de latitude e longitude com a ultima localização
        LatLng latLng = new LatLng(curLoc.getLatitude(), curLoc.getLongitude());
        //Criar marcador nessa localização
        final MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Voce esta aqui!");
        //Definir zoom da camera
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
        googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Definindo função para pegar localização do local fixo do usuario no clique do marcador
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this)
                        .setTitle("Definir")
                        .setMessage("Definir esse local como seu local de trabalho")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LocalFixoLat = markerOptions.getPosition().latitude;
                                LocalFixoLng = markerOptions.getPosition().longitude;
                                Map<String, Object>updates = new HashMap<>();
                                //Atualizando database com o Local fixo
                                updates.put("LocalFixoLat",LocalFixoLat);
                                updates.put("LocalFixoLng",LocalFixoLng);
                                userRef.updateChildren(updates);
                                //Mandando para serviço localizar em background a localização
                                Intent intServ = new Intent(MapActivity.this,LocationService.class);
                                ContextCompat.startForegroundService(getApplicationContext(),intServ);
                            }
                        });
                        builder.show();
                        return false;
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Definir um novo marcador se clicado em outro local do mapa
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Definir casa aqui?");
                googleMap.clear();
                googleMap.addMarker(markerOptions);
            }
        });
    }


}
