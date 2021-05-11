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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.util.ConfigPermissoes;

public class PratoActivity extends AppCompatActivity {

    String[] permissoes = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    ImageView imageViewPrato;
    Button buttonCadastrar;
    int CAMERA = 1000;
    int GALERIA = 2000;
    AlertDialog.Builder builder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prato);

        initView();

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Você precisa conceder pelo menos uma permissão");
        builder.setNeutralButton("Cancelar", null);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PratoActivity.this, "OI", Toast.LENGTH_SHORT).show();
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

    private void initView() {
        imageViewPrato = findViewById(R.id.img_avatar);
        buttonCadastrar = findViewById(R.id.btn_cadastrarPrato);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            imageViewPrato.setImageURI(data.getData());
        } else if(requestCode == CAMERA && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageViewPrato.setImageBitmap(bitmap);
        }
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