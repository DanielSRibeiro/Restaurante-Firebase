package com.example.restaurant_firebase;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.util.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;

public class PratoAdapter extends RecyclerView.Adapter<PratoAdapter.viewHolder> {

    ArrayList<PratoDto> listPrato;
    OnClickItemPratoListener listener;
    StorageReference storageReference;

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
        holder.textViewNome.setText(prato.getNome());
        holder.textViewDescricao.setText(prato.getDescricao());
        storageReference = ConfigFirebase.getFirebaseStorage().child("imagens").child("prato").child(prato.getImagem());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(holder.imageViewPrato);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPrato.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewPrato;
        TextView textViewNome, textViewDescricao;

        public viewHolder(@NonNull View itemView, OnClickItemPratoListener listener) {
            super(itemView);

            textViewNome = itemView.findViewById(R.id.txt_nomePratoAdapter);
            textViewDescricao = itemView.findViewById(R.id.txt_descricaoAdapter);
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