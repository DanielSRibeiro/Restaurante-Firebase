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
import com.example.restaurant_firebase.presentation.LoginFragment;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarUsuarioFragment extends Fragment implements CadastrarUsuarioContract.View{

    Button buttonVoltar, buttonCadastrar;
    EditText editTextEmail, editTextSenha;
    TextInputLayout textInputEmail, textInputSenha;
    FirebaseAuth firebaseAuth;

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
                transactionFragment(new LoginFragment());
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

    private void cadastrarUsuario(String email, String senha) {
        if(validacaoFormulario(email, senha)){
            firebaseAuth = ConfigFirebase.getFirebaseAuth();
            firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Verifique seu E-Mail para poder continuar", Toast.LENGTH_SHORT).show();
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                        transactionFragment(new LoginFragment());
                    }
                    else{
                        try { throw task.getException(); }
                        catch (FirebaseAuthWeakPasswordException ex){ Toast.makeText(getActivity(), "Senha fraca!!!", Toast.LENGTH_SHORT).show(); }
                        catch (FirebaseAuthEmailException ex){ Toast.makeText(getActivity(), "Padrão de E-Mail incorrento!!!", Toast.LENGTH_SHORT).show(); }
                        catch (FirebaseAuthUserCollisionException ex){ Toast.makeText(getActivity(), "E-Mail já cadastrado!!!", Toast.LENGTH_SHORT).show(); }
                        catch (Exception exception) { exception.printStackTrace(); }
                    }
                }
            });
        }

    }

    private void transactionFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_login, fragment);
        transaction.commit();
    }

}