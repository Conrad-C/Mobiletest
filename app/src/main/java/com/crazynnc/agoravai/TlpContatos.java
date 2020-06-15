package com.crazynnc.agoravai;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TlpContatos extends Fragment {
    TextView listaContatos;
    DatabaseReference refNomeEmail = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState){
        View vi = inflater.inflate(R.layout.fragment_tela_p_l_contatos,container,false);

        listaContatos = vi.findViewById(R.id.listaContatos);
        arrayList = new ArrayList<String>();

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }else{
            getContacts();
        }
        return vi;
    }

    @SuppressLint("SetTextI18n")
    private void getContacts() {
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(cursor.moveToNext()){
        String nome=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String numero=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        nome = nome.replace(",","").replace(".","").replace("#","").replace("$","")
                .replace("[","").replace("]","");

        numero = numero.replaceAll(",","");
        arrayList.add(nome+"\n"+numero+"\n\n");
        listaContatos.setText(arrayList.toString());
        Map<String, Object> updates = new HashMap<String,Object>();
        updates.put("Numero",numero);
        refNomeEmail.child("Contatos").child(nome).updateChildren(updates);
    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getContacts();
            }
        }
    }
}
