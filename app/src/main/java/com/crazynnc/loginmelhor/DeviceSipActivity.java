package com.crazynnc.loginmelhor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceSipActivity extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_devicesip,container,false);

        ExpandableListView expandableListView = view.findViewById(R.id.expandablelist);
        HashMap<String, List<String>> item = new HashMap<>();

        ArrayList<String> deviceGroup = new ArrayList<>();

        deviceGroup.add("Remover device");

        item.put("Device SIP - 8014 - Filial Manaus - "/*+"Confirmado ou desabilitado"*/, deviceGroup);

        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(item);
        expandableListView.setAdapter(expandableListAdapter);
        return view;
    }
}
