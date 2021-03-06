package com.example.restaurant_firebase.presentation.login;

import com.example.restaurant_firebase.presentation.consulta.ConsultaFragment;
import com.example.restaurant_firebase.service.FirebaseServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.Presenter{
    private FirebaseAuth firebaseAuth;
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void realizarLogin(String email, String senha) {
        firebaseAuth = FirebaseServices.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task< AuthResult > task) {
                        if(task.isSuccessful()){
                            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                                view.mudarFragment(new ConsultaFragment());
                            }else{
                                view.mostrarToast("Verifique o seu E-Mail para poder realizar o Login!!!");
                            }
                        } else{
                            view.mostrarToast("E-Mail ou senha esta incorreta!!!");
                        }
                    }
                });
    }

    @Override
    public void detruirView() {
        this.view = null;
    }
}
