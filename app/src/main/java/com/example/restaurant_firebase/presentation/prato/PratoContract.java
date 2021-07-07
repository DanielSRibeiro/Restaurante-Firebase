package com.example.restaurant_firebase.presentation.prato;

import android.graphics.Bitmap;
import android.net.Uri;

public interface PratoContract {

    interface View{
        void cadastradoSucesso();
    }

    interface Presenter{
        void cadastrarImagem(String nome, String valor, String descricao, Bitmap bitmap);

        void cadastrarPrato(String nome, String valor, String descricao, Uri downloadUrl);
    }
}
