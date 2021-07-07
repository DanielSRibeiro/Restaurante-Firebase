package com.example.restaurant_firebase.presentation.CadastrarUsuario;

import com.example.restaurant_firebase.presentation.Login.LoginFragment;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarUsuarioPresenter implements CadastrarUsuarioContract.Presenter{

    private FirebaseAuth firebaseAuth;
    private CadastrarUsuarioContract.View view;

    public CadastrarUsuarioPresenter(CadastrarUsuarioContract.View view) {
        this.view = view;
    }

    @Override
    public void cadastrarUsuario(String email, String senha) {
        firebaseAuth = ConfigFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    view.mostrarToast("Verifique seu E-Mail para poder continuar");
                    view.mudarFragment(new LoginFragment());
                }
                else{
                    try { throw task.getException(); }
                    catch (FirebaseAuthWeakPasswordException ex){ view.mostrarToast("Senha fraca!!!"); }
                    catch (FirebaseAuthEmailException ex){ view.mostrarToast("Padrão de E-Mail incorrento!!!"); }
                    catch (FirebaseAuthUserCollisionException ex){ view.mostrarToast("E-Mail já cadastrado!!!"); }
                    catch (Exception exception) { exception.printStackTrace(); }
                }
            }
        });
    }

    @Override
    public void destruirView() {
        this.view = null;
    }
}
