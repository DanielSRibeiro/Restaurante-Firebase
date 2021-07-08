package com.example.restaurant_firebase.presentation.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.presentation.cadastrarUsuario.CadastrarUsuarioFragment;
import com.example.restaurant_firebase.presentation.MainActivity;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment implements LoginContract.View{

    Button buttonLogin;
    TextView textViewCadastrar;
    EditText editTextEmail, editTextSenha;
    TextInputLayout textInputEmail, textInputSenha;
    SignInButton googleButton;
    LoginContract.Presenter presenter = new LoginPresenter(this);
    boolean valido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        onClick();

        return view;
    }

    private void initView(View view) {
        textViewCadastrar = view.findViewById(R.id.txt_cadastrar);
        googleButton = view.findViewById(R.id.signInButton);
        buttonLogin = view.findViewById(R.id.btn_login);
        editTextEmail = view.findViewById(R.id.edt_email);
        editTextSenha = view.findViewById(R.id.edt_senha);
        textInputEmail = view.findViewById(R.id.textInputEmail);
        textInputSenha = view.findViewById(R.id.textInputSenha);
    }

    private void onClick() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String senha = editTextSenha.getText().toString();
                logarUsuario(email, senha);
            }
        });
        textViewCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarFragment(new CadastrarUsuarioFragment());
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).signIn();
            }
        });
    }

    private void validacaoFormulario(String editText, TextInputLayout textInput) {
        if(editText.length() < 1){
            valido = false;
            textInput.setError("Por favor preencha");
        }else{
            textInput.setError("");
        }
    }

    private void logarUsuario(String email, String senha) {
        valido = true;
        validacaoFormulario(email, textInputEmail);
        validacaoFormulario(senha, textInputSenha);
        if(valido){
            presenter.realizarLogin(email, senha);
        }
    }

    @Override
    public void mostrarToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void mudarFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().
                replace(R.id.frameLayout_login, fragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detruirView();
    }
}