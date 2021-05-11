package com.example.restaurant_firebase.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    Button buttonLogin;
    TextView textViewCadastrar;
    EditText editTextEmail, editTextSenha;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        textViewCadastrar = view.findViewById(R.id.txt_cadastrar);
        buttonLogin = view.findViewById(R.id.btn_login);
        editTextEmail = view.findViewById(R.id.edt_email);
        editTextSenha = view.findViewById(R.id.edt_senha);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = ConfigFirebase.getFirebaseAuth();
                firebaseAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextSenha.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ConsultaFragment consultaFragment = new ConsultaFragment();
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frameLayout_login, consultaFragment);
                            transaction.commit();
                        } else{
                            Toast.makeText(getActivity(), "E-Mail ou senha esta incorreta!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        textViewCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CadastrarUsuarioFragment cadastrarUsuarioFragment = new CadastrarUsuarioFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_login, cadastrarUsuarioFragment);
                transaction.commit();
            }
        });

        return view;
    }
}