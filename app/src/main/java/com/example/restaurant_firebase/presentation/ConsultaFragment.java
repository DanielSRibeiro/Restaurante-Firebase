package com.example.restaurant_firebase.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.restaurant_firebase.presentation.adapter.OnClickItemPratoListener;
import com.example.restaurant_firebase.presentation.adapter.PratoAdapter;
import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ConsultaFragment extends Fragment implements OnClickItemPratoListener {

    private static final String TAG = "ConsultaFragment";
    FloatingActionButton buttonAdicionar;
    RecyclerView recyclerViewPratos;
    ArrayList<PratoDto> listPrato = new ArrayList<>();
    DatabaseReference databaseReference;
    AlertDialog.Builder builder;
    StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta, container, false);
        initView(view);
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PratoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView(View view) {
        buttonAdicionar = view.findViewById(R.id.fab_adiciona);
        recyclerViewPratos = view.findViewById(R.id.recycler_consultar);
        ((MainActivity) getActivity()).setToolbarTitle("Menu");
        initDatabase();
    }

    private void initDatabase() {
        databaseReference = ConfigFirebase.getDatabaseReference().child("pratos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listPrato.clear();
                for (DataSnapshot prato: snapshot.getChildren()){
                    listPrato.add(prato.getValue(PratoDto.class));
                }
                updateAdapter();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    private void updateAdapter() {
        PratoAdapter adapter = new PratoAdapter(listPrato,this);
        recyclerViewPratos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPratos.setAdapter(adapter);
    }

    @Override
    public void onClick(PratoDto pratoDto) {
        Intent intent = new Intent(getActivity(), PratoActivity.class);
        intent.putExtra("KEY_NOME", pratoDto.getNome());
        intent.putExtra("KEY_VALOR", pratoDto.getValor());
        intent.putExtra("KEY_DESCR", pratoDto.getDescricao());
        intent.putExtra("KEY_IMAGEM", pratoDto.getImagem());
        startActivity(intent);
    }

    @Override
    public void onLongClick(PratoDto pratoDto) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deseja realmente excluir esse prato?");
        builder.setNegativeButton("Não", null);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference = ConfigFirebase.getDatabaseReference().child("pratos").child(pratoDto.getNome());
                databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Removido values com sucesso");
                        }
                    }
                });
                storageReference = ConfigFirebase.getFirebaseStorage().child("imagens").child("prato").child(pratoDto.getNome()+".jpeg");
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Removido imagem com sucesso");
                        }
                    }
                });
                updateAdapter();
            }
        });
        builder.show();
    }

}