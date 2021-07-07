package com.example.restaurant_firebase.presentation.consulta;

import android.util.Log;
import android.widget.Toast;

import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ConsultaPresenter implements ConsultaContract.Presenter{

    private static final String TAG = "ConsultaPresenter";
    private ConsultaContract.View view;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    public ConsultaPresenter(ConsultaContract.View view) {
        this.view = view;
    }

    @Override
    public void getAllPratos(ArrayList<PratoDto> listPrato) {
        databaseReference = ConfigFirebase.getDatabaseReference().child("pratos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listPrato.clear();
                for (DataSnapshot prato: snapshot.getChildren()){
                    listPrato.add(prato.getValue(PratoDto.class));
                }
                view.atualizarRecycler();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void removerPrato(PratoDto pratoDto) {
        databaseReference = ConfigFirebase.getDatabaseReference().child("pratos").child(pratoDto.getNome());
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    view.atualizarRecycler();
                }else{
                    view.mostrarToast("Removido values com sucesso");
                }
            }
        });
    }

    @Override
    public void removerFoto(PratoDto pratoDto) {
        storageReference = ConfigFirebase.getFirebaseStorage().child("imagens").child("prato").child(pratoDto.getNome()+".jpeg");
        storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    view.atualizarRecycler();
                }else{
                    view.mostrarToast("Erro ao remover imagem");
                }
            }
        });
    }
}
