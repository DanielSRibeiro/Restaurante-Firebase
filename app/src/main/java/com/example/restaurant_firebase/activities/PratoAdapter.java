package com.example.restaurant_firebase.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.R;

import java.util.ArrayList;

public class PratoAdapter extends RecyclerView.Adapter<PratoAdapter.viewHolder> {

    ArrayList<PratoDto> listPrato;
    OnClickItemPratoListener listener;

    public PratoAdapter(ArrayList<PratoDto> listPrato, OnClickItemPratoListener listener) {
        this.listPrato = listPrato;
        this.listener = listener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prato_adapter, parent, false);
        return new viewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listPrato.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        public viewHolder(@NonNull View itemView, OnClickItemPratoListener listener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(listPrato.get(getAdapterPosition()));
                }
            });
        }
    }

}
