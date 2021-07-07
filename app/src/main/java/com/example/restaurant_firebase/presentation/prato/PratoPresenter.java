package com.example.restaurant_firebase.presentation.prato;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class PratoPresenter implements PratoContract.Presenter{
    private PratoContract.View view;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth = ConfigFirebase.getFirebaseAuth();

    public PratoPresenter(PratoContract.View view) {
        this.view = view;
    }

    @Override
    public void cadastrarImagem(String nome, String valor, String descricao, Bitmap bitmap) {
        storageReference = ConfigFirebase.getFirebaseStorage().child("imagens").child("prato").child(nome+".jpeg");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(byteArray);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    task.getException().printStackTrace();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUrl = task.getResult();
                    cadastrarPrato(nome, valor, descricao, downloadUrl);
                }
            }
        });

    }

    @Override
    public void cadastrarPrato(String nome, String valor, String descricao, Uri downloadUrl) {
        PratoDto pratoDto = new PratoDto(nome, valor, descricao, downloadUrl.toString(),
                firebaseAuth.getCurrentUser().getEmail());
        databaseReference = ConfigFirebase.getDatabaseReference().child("pratos").child(pratoDto.getNome());
        databaseReference.setValue(pratoDto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    view.cadastradoSucesso();
                }
            }
        });
    }
}
