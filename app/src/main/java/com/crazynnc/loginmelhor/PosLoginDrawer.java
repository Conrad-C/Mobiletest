package com.crazynnc.loginmelhor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PosLoginDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    String frag;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setTitle("Principal");
        if(getIntent().getStringExtra("frag")!=null){
            frag = getIntent().getStringExtra("frag");
            if(frag.equals("Ramal")) {
                toolbar.setTitle("Ramal Movel");
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new RamalActivity());
                fragmentTransaction.commit();
                navigationView.setNavigationItemSelectedListener(this);
            }
        }else{
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container_fragment, new PrincipalActivity());
            fragmentTransaction.commit();
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.m_principal){
            toolbar.setTitle("Principal");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new PrincipalActivity());
            fragmentTransaction.commit();
        }if(menuItem.getItemId()==R.id.m_ramal){
            toolbar.setTitle("Ramal Movel");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new RamalActivity());
            fragmentTransaction.commit();
        }if(menuItem.getItemId()==R.id.m_perfil){
            toolbar.setTitle("Perfil");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new PerfilActivity());
            fragmentTransaction.commit();
        }if(menuItem.getItemId()==R.id.m_devicesip){
            toolbar.setTitle("Device SIP");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new DeviceSipActivity());
            fragmentTransaction.commit();
        }if(menuItem.getItemId()==R.id.m_logout){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this,"Saiu",Toast.LENGTH_SHORT);
            startActivity(new Intent(this,MainActivity.class));
        }if(menuItem.getItemId()==R.id.m_definirlocal){
            toolbar.setTitle("Definir local");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new PreferenciasActivity());
            fragmentTransaction.commit();
        }if(menuItem.getItemId()==R.id.m_seguranca) {
            toolbar.setTitle("Definir local");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new SegurancaActivity());
            fragmentTransaction.commit();
        }
        return true;
    }
}
