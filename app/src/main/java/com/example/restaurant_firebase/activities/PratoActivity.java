package com.example.restaurant_firebase.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.example.restaurant_firebase.util.ConfigPermissoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PratoActivity extends AppCompatActivity {

    String[] permissoes = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    ImageView imageViewPrato;
    Button buttonCadastrar;
    EditText editTextNome, editTextValor, editTextDescricao;
    Bitmap bitmap;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    int CAMERA = 1000;
    int GALERIA = 2000;
    AlertDialog.Builder builder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prato);

        initView();

        firebaseAuth = ConfigFirebase.getFirebaseAuth();

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Você precisa conceder pelo menos uma permissão");
        builder.setNeutralButton("Cancelar", null);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = editTextNome.getText().toString();
                String valor = editTextValor.getText().toString();
                String descricao = editTextDescricao.getText().toString();

                firebaseAuth = ConfigFirebase.getFirebaseAuth();
                boolean cadastradoImagem = uploadImagem(nome);

                PratoDto pratoDto = new PratoDto(nome, valor,
                        descricao, nome+".JPEG", firebaseAuth.getCurrentUser().getEmail());
                databaseReference = ConfigFirebase.getDatabaseReference().child("pratos").child(pratoDto.getNome());
                if(cadastradoImagem){
                    databaseReference.setValue(pratoDto).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PratoActivity.this, "Sucesso ao Inserir o Prato", Toast.LENGTH_SHORT).show();
                                builder.setMessage("Deseja voltar para tela inicial?");
                                builder.setNeutralButton("", null);
                                builder.setNegativeButton("Não", null);
                                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                        }
                    });
                }
            }
        });

        imageViewPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigPermissoes.validarPermissoes(PratoActivity.this, permissoes, 1);

                if(ActivityCompat.checkSelfPermission(PratoActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED){
                    buttonPositive();
                }
                if (ActivityCompat.checkSelfPermission(PratoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
                    buttonNegative();
                }
                if(ActivityCompat.checkSelfPermission(PratoActivity.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PratoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){
                    builder.show();
                }
            }
        });
    }

    private boolean uploadImagem(String nome) {
        if(bitmap != null){
            imageViewPrato.setImageBitmap(bitmap);
            storageReference = ConfigFirebase.getFirebaseStorage().child("imagens").child("prato").child(nome+".JPEG");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            storageReference.putBytes(byteArray);
            return true;
        } else{
            Toast.makeText(this, "Por favor selecionar alguma imagem para o seu Prato!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void initView() {
        imageViewPrato = findViewById(R.id.img_avatar);
        buttonCadastrar = findViewById(R.id.btn_cadastrarPrato);
        editTextNome = findViewById(R.id.edt_nomePrato);
        editTextValor = findViewById(R.id.edt_valorPrato);
        editTextDescricao = findViewById(R.id.edt_descricaoPrato);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Adicionar novo Prato");
    }

    private void buttonNegative() {
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA);
            }
        });
    }

    private void buttonPositive() {
        builder.setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALERIA && resultCode == RESULT_OK){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode == CAMERA && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
        }
        imageViewPrato.setImageBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < permissions.length ; i++) {
            if (permissions[i].equals("android.permission.CAMERA") && grantResults[i] == 0){
                Toast.makeText(this, "Concedida a Camera!!!", Toast.LENGTH_SHORT).show();
                buttonPositive();
            } else if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE") && grantResults[i] == 0){
                Toast.makeText(this, "Concedida na Galeria!!!", Toast.LENGTH_SHORT).show();
                buttonNegative();
            }
        }
        builder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}