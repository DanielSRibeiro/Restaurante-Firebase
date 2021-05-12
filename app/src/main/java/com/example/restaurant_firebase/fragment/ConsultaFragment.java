package com.example.restaurant_firebase.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.restaurant_firebase.OnClickItemPratoListener;
import com.example.restaurant_firebase.activities.PratoActivity;
import com.example.restaurant_firebase.PratoAdapter;
import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ConsultaFragment extends Fragment implements OnClickItemPratoListener {

    FloatingActionButton buttonAdicionar;
    RecyclerView recyclerViewPratos;
    ArrayList<PratoDto> listPrato = new ArrayList<>();
    DatabaseReference databaseReference;
    AlertDialog.Builder builder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta, container, false);

        buttonAdicionar = view.findViewById(R.id.fab_adiciona);
        recyclerViewPratos = view.findViewById(R.id.recycler_consultar);

        databaseReference = ConfigFirebase.getDatabaseReference().child("pratos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPrato.clear();
                for (DataSnapshot prato: snapshot.getChildren()){
                    listPrato.add(prato.getValue(PratoDto.class));
                }
                updateAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PratoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateAdapter() {
        PratoAdapter adapter = new PratoAdapter(listPrato,this);
        recyclerViewPratos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPratos.setAdapter(adapter);
    }

    @Override
    public void onClick(PratoDto pratoDto) {
        Toast.makeText(getActivity(), pratoDto.getNome()+" Funcionou", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongClick(PratoDto pratoDto) {
        Toast.makeText(getActivity(), "Long", Toast.LENGTH_SHORT).show();
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deseja realmente excluir esse prato?");
        builder.setNegativeButton("Não", null);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference = ConfigFirebase.getDatabaseReference().child("pratos").child(pratoDto.getNome());
                databaseReference.removeValue();
            }
        });
        builder.show();
        updateAdapter();
    }
}