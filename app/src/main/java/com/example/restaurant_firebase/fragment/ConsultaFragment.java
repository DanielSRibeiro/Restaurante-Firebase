package com.example.restaurant_firebase.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurant_firebase.activities.OnClickItemPratoListener;
import com.example.restaurant_firebase.activities.PratoActivity;
import com.example.restaurant_firebase.activities.PratoAdapter;
import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ConsultaFragment extends Fragment implements OnClickItemPratoListener {

    FloatingActionButton buttonAdicionar;
    RecyclerView recyclerViewPratos;
    ArrayList<PratoDto> listPrato = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta, container, false);

        buttonAdicionar = view.findViewById(R.id.fab_adiciona);
        recyclerViewPratos = view.findViewById(R.id.recycler_consultar);

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PratoActivity.class);
                startActivity(intent);
            }
        });

        //falta colocar os valores no arrayList
        updateAdapter();
        return view;
    }

    private void updateAdapter() {
        PratoAdapter adapter = new PratoAdapter(listPrato,this);
        recyclerViewPratos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPratos.setAdapter(adapter);
    }

    @Override
    public void onClick(PratoDto pratoDto) {

    }
}