package com.example.restaurant_firebase.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_firebase.R;
import com.example.restaurant_firebase.model.PratoDto;
import com.squareup.picasso.Picasso;

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
        PratoDto prato = listPrato.get(position);
        Picasso.get().load(prato.getImagem()).into(holder.imageViewPrato);
        holder.textViewNome.setText(prato.getNome());
        holder.textViewDescricao.setText(prato.getDescricao());
        holder.textViewPreco.setText("R$"+prato.getValor());

    }

    @Override
    public int getItemCount() {
        return listPrato.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewPrato;
        TextView textViewNome, textViewDescricao, textViewPreco;

        public viewHolder(@NonNull View itemView, OnClickItemPratoListener listener) {
            super(itemView);

            textViewNome = itemView.findViewById(R.id.txt_nomePratoAdapter);
            textViewDescricao = itemView.findViewById(R.id.txt_descricaoAdapter);
            textViewPreco = itemView.findViewById(R.id.txt_precoAdapter);
            imageViewPrato = itemView.findViewById(R.id.img_avatarAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(listPrato.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(listPrato.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }
}
