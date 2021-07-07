package com.example.restaurant_firebase.presentation.consulta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.restaurant_firebase.presentation.MainActivity;
import com.example.restaurant_firebase.presentation.prato.PratoActivity;
import com.example.restaurant_firebase.presentation.adapter.OnClickItemPratoListener;
import com.example.restaurant_firebase.presentation.adapter.PratoAdapter;
import com.example.restaurant_firebase.model.PratoDto;
import com.example.restaurant_firebase.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ConsultaFragment extends Fragment implements OnClickItemPratoListener, ConsultaContract.View{

    FloatingActionButton buttonAdicionar;
    RecyclerView recyclerViewPratos;
    ArrayList<PratoDto> listPrato = new ArrayList<>();
    AlertDialog.Builder builder;
    ConsultaContract.Presenter presenter = new ConsultaPresenter(this);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consulta, container, false);
        initView(view);
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PratoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initView(View view) {
        buttonAdicionar = view.findViewById(R.id.fab_adiciona);
        recyclerViewPratos = view.findViewById(R.id.recycler_consultar);
        presenter.getAllPratos(listPrato);
        ((MainActivity) getActivity()).setToolbarTitle("Menu");
    }

    @Override
    public void onClick(PratoDto pratoDto) {
        Intent intent = new Intent(getActivity(), PratoActivity.class);
        intent.putExtra("KEY_NOME", pratoDto.getNome());
        intent.putExtra("KEY_VALOR", pratoDto.getValor());
        intent.putExtra("KEY_DESCR", pratoDto.getDescricao());
        intent.putExtra("KEY_IMAGEM", pratoDto.getImagem());
        startActivity(intent);
    }

    @Override
    public void onLongClick(PratoDto pratoDto) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Deseja realmente excluir esse prato?");
        builder.setNegativeButton("Não", null);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.removerPrato(pratoDto);
                presenter.removerFoto(pratoDto);
            }
        });
        builder.show();
    }

    @Override
    public void mostrarToast(String toast) {
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void atualizarRecycler() {
        PratoAdapter adapter = new PratoAdapter(listPrato,this);
        recyclerViewPratos.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewPratos.setAdapter(adapter);
    }
}