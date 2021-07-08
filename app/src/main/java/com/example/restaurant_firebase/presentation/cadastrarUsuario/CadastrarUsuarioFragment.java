package com.example.restaurant_firebase.presentation.cadastrarUsuario;

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
import com.example.restaurant_firebase.presentation.login.LoginFragment;
import com.google.android.material.textfield.TextInputLayout;

public class CadastrarUsuarioFragment extends Fragment implements CadastrarUsuarioContract.View{

    Button buttonVoltar, buttonCadastrar;
    EditText editTextEmail, editTextSenha;
    TextInputLayout textInputEmail, textInputSenha;
    CadastrarUsuarioContract.Presenter presenter = new CadastrarUsuarioPresenter(this);;
    boolean valido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_usuario, container, false);
        initView(view);
        onClick();

        return view;
    }

    private void initView(View view) {
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
                cadastrarUsuario(editTextEmail.getText().toString(), editTextSenha.getText().toString());
            }
        });
    }

    private void cadastrarUsuario(String email, String senha) {
        valido = true;
        validacaoFormulario(email, textInputEmail);
        validacaoFormulario(senha, textInputSenha);
        if(valido){
            presenter.cadastrarUsuario(email, senha);
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

    @Override
    public void mostrarToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mudarFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_login, fragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destruirView();
    }
}