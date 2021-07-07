package com.example.restaurant_firebase.presentation.consulta;

import com.example.restaurant_firebase.model.PratoDto;

import java.util.ArrayList;

public interface ConsultaContract {

    interface View{
        void mostrarToast(String toast);

        void atualizarRecycler();
    }

    interface Presenter{
        void getAllPratos(ArrayList<PratoDto> listPrato);

        void removerPrato(PratoDto pratoDto);

        void removerFoto(PratoDto pratoDto);
    }
}
