package com.example.restaurant_firebase.presentation.login;

import androidx.fragment.app.Fragment;

public interface LoginContract {

    interface View{
        void mostrarToast(String toast);

        void mudarFragment(Fragment fragment);
    }

    interface Presenter{
        void realizarLogin(String email, String senha);

        void detruirView();
    }
}
