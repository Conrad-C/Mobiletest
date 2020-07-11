package com.crazynnc.loginmelhor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RamalActivity extends Fragment {

    Button addnumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ramalmovel,container,false);
        getActivity().setTitle("Ramal Movel");
        ExpandableListView expandableListView = view.findViewById(R.id.expandablelist);
        HashMap<String, List<String>> item = new HashMap<>();
        ArrayList<String> deviceGroup = new ArrayList<>();

        deviceGroup.add("Remover numero");

        item.put("Ramal Movel - (48) 99876-5432", deviceGroup);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(item);
        expandableListView.setAdapter(expandableListAdapter);

        addnumber = view.findViewById(R.id.autenticatenumber);

        addnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RamalVincular.class));
            }
        });

        return view;
    }
}
