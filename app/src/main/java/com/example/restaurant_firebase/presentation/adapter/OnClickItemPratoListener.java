package com.example.restaurant_firebase.presentation.adapter;

import com.example.restaurant_firebase.model.PratoDto;

public interface OnClickItemPratoListener {
    void onClick(PratoDto pratoDto);

    void onLongClick(PratoDto pratoDto);
}
