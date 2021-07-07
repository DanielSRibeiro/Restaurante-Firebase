package com.example.restaurant_firebase.presentation.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.presentation.CadastrarUsuario.CadastrarUsuarioFragment;
import com.example.restaurant_firebase.presentation.ConsultaFragment;
import com.example.restaurant_firebase.presentation.MainActivity;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements LoginContract.View{

    Button buttonLogin;
    TextView textViewCadastrar;
    EditText editTextEmail, editTextSenha;
    TextInputLayout textInputEmail, textInputSenha;
    SignInButton googleButton;
    LoginContract.Presenter presenter = new LoginPresenter(this);

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
                boolean preenchido = validacaoFormulario(email, senha);
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

    private void logarUsuario(String email, String senha) {
        if(validacaoFormulario(email, senha)){
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