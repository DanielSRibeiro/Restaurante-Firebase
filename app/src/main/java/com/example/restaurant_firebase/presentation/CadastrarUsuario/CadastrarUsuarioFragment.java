package com.example.restaurant_firebase.presentation.CadastrarUsuario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.presentation.Login.LoginFragment;
import com.google.android.material.textfield.TextInputLayout;

public class CadastrarUsuarioFragment extends Fragment implements CadastrarUsuarioContract.View{

    Button buttonVoltar, buttonCadastrar;
    EditText editTextEmail, editTextSenha;
    TextInputLayout textInputEmail, textInputSenha;
    CadastrarUsuarioContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_usuario, container, false);
        initView(view);
        onClick();

        return view;
    }

    private void initView(View view) {
        presenter = new CadastrarUsuarioPresenter(this);
        buttonVoltar = view.findViewById(R.id.btn_voltar);
        buttonCadastrar = view.findViewById(R.id.btn_cadastrar);
        editTextEmail = view.findViewById(R.id.edt_emailCadastrar);
        editTextSenha = view.findViewById(R.id.edt_senhaCadastrar);
        textInputEmail = view.findViewById(R.id.textInputEmailCadastro);
        textInputSenha = view.findViewById(R.id.textInputSenhaCadastrar);
    }

    private void onClick() {
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarFragment(new LoginFragment());
            }
        });
        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String senha = editTextSenha.getText().toString();
                cadastrarUsuario(email, senha);
            }
        });
    }

    private void cadastrarUsuario(String email, String senha) {
        if(validacaoFormulario(email, senha)){
            presenter.cadastrarUsuario(email, senha);
        }

    }

    private boolean validacaoFormulario(String email, String senha) {
        boolean valido = true;
        if(email.isEmpty()){
            valido = false;
            textInputEmail.setError("Por favor preencha o E-Mail");
        }else{
            textInputEmail.setError("");
        }

        if(senha.isEmpty()){
            valido = false;
            textInputSenha.setError("Por favor preencha a Senha");

        }else{
            textInputSenha.setError("");
        }

        return valido;
    }

    @Override
    public void mostrarToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mudarFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_login, fragment);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destruirView();
    }
}