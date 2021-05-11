package com.example.restaurant_firebase.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarUsuarioFragment extends Fragment {

    Button buttonVoltar, buttonCadastrar;
    EditText editTextEmail, editTextSenha;
    AlertDialog.Builder builder;
    LoginFragment loginFragment = new LoginFragment();
    FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastrar_usuario, container, false);

        buttonVoltar = view.findViewById(R.id.btn_voltar);
        buttonCadastrar = view.findViewById(R.id.btn_cadastrar);
        editTextEmail = view.findViewById(R.id.edt_emailCadastrar);
        editTextSenha = view.findViewById(R.id.edt_senhaCadastrar);

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_login, loginFragment);
                transaction.commit();
            }
        });

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth = ConfigFirebase.getFirebaseAuth();
                firebaseAuth = ConfigFirebase.getFirebaseAuth();
                firebaseAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextSenha.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    alertDialogVoltar();
                                }
                                else{
                                    try {
                                        throw task.getException();
                                    }
                                    catch (FirebaseAuthWeakPasswordException ex){
                                        Toast.makeText(getActivity(), "Senha fraca!!!", Toast.LENGTH_SHORT).show();
                                    }
                                    catch (FirebaseAuthEmailException ex){
                                        Toast.makeText(getActivity(), "Padrão de E-Mail incorrento!!!", Toast.LENGTH_SHORT).show();
                                    }
                                    catch (FirebaseAuthUserCollisionException ex){
                                        Toast.makeText(getActivity(), "E-Mail já cadastrado!!!", Toast.LENGTH_SHORT).show();
                                    }
                                    catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void alertDialogVoltar() {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Usuário cadastrado");
        builder.setMessage("Usuario cadastrado. Deseja voltar para a tela de Login ?");
        builder.setNegativeButton("Não", null);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout_login, loginFragment);
                transaction.commit();
            }
        });
        builder.show();
    }
}