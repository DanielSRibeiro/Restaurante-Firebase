package com.example.restaurant_firebase.presentation.cadastrarUsuario;

import androidx.fragment.app.Fragment;

public interface CadastrarUsuarioContract {

    interface View{
        void mostrarToast(String toast);

        void mudarFragment(Fragment fragment);
    }

    interface Presenter{
        void cadastrarUsuario(String email, String senha);

        void destruirView();
    }

}
