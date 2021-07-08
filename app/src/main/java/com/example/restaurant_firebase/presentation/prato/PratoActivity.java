package com.example.restaurant_firebase.presentation.prato;

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
import com.example.restaurant_firebase.service.PermissoesServices;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PratoActivity extends AppCompatActivity implements PratoContract.View{

    String[] permissoes = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
    ImageView imageViewPrato;
    Button buttonCadastrar;
    EditText editTextNome, editTextValor, editTextDescricao;
    TextInputLayout textInputNome, textInputValor, textInputDescricao;
    Bitmap bitmap;
    int CAMERA = 1000;
    int GALERIA = 2000;
    AlertDialog.Builder builder;
    PratoContract.Presenter presenter = new PratoPresenter(this);
    boolean valido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prato);

        initView();
        onClick();
    }

    private void initView() {
        imageViewPrato = findViewById(R.id.img_avatar);
        buttonCadastrar = findViewById(R.id.btn_cadastrarPrato);
        editTextNome = findViewById(R.id.edt_nomePrato);
        editTextValor = findViewById(R.id.edt_valorPrato);
        editTextDescricao = findViewById(R.id.edt_descricaoPrato);
        textInputNome = findViewById(R.id.textInputLayoutNome);
        textInputValor = findViewById(R.id.textInputLayoutValor);
        textInputDescricao = findViewById(R.id.textInputLayoutDescricao);

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Você precisa conceder pelo menos uma permissão");
        builder.setNeutralButton("Cancelar", null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Adicionar novo Prato");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            editTextNome.setText(bundle.getString("KEY_NOME"));
            editTextValor.setText(bundle.getString("KEY_VALOR"));
            editTextDescricao.setText(bundle.getString("KEY_DESCR"));
            Picasso.get().load(bundle.getString("KEY_IMAGEM")).into(imageViewPrato);

            editTextNome.setEnabled(false);
            editTextValor.setEnabled(false);
            editTextDescricao.setEnabled(false);
            imageViewPrato.setEnabled(false);
            buttonCadastrar.setVisibility(View.INVISIBLE);
        }
    }

    private void onClick() {
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImagem(editTextNome.getText().toString(), editTextValor.getText().toString(), editTextDescricao.getText().toString());
            }
        });

        imageViewPrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissoesServices.validarPermissoes(PratoActivity.this, permissoes, 1);

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

    private void uploadImagem(String nome, String valor, String descricao) {
        valido = true;
        validacaoFormulario(nome, textInputNome);
        validacaoFormulario(valor, textInputValor);
        validacaoFormulario(descricao, textInputDescricao);
        if(valido){
            if(bitmap != null){
                imageViewPrato.setImageBitmap(bitmap);
                presenter.cadastrarImagem(nome, valor, descricao, bitmap);
            } else{
                mostrarToast("Por favor seleciona alguma imagem para o seu Prato!!!");
            }
        }
    }

    private void validacaoFormulario(String editText, TextInputLayout textInput) {
        if(editText.length() < 1){
            valido = false;
            textInput.setError("Por favor preencha");
        }else{
            textInput.setError("");
        }
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
                mostrarToast("Concedida a Camera!!!");
                buttonPositive();
            } else if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE") && grantResults[i] == 0){
                mostrarToast("Concedida na Galeria!!!");
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

    @Override
    public void cadastradoSucesso() {
        mostrarToast("Sucesso ao cadastrar o prato");
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

    public void mostrarToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }
}