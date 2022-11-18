package com.example.tarea3.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.example.tarea3.R;
import com.example.tarea3.adapters.MarcaAdapter;
import com.example.tarea3.datamodels.MarcaModel;
import com.example.tarea3.dbModels.MarcaDB;

public class ListMarcasFragment extends Fragment {

    public ListMarcasFragment() {
    }

    public static ListMarcasFragment newInstance() {
        ListMarcasFragment fragment = new ListMarcasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_marcas, container, false);

        List<MarcaModel> listMarca = new ArrayList<MarcaModel>();
        RecyclerView marcaRecyclerView = view.findViewById(R.id.marcaRecyclerView);
        MarcaDB marcaDB = new MarcaDB(this.getContext()).open();
        Cursor marcas = marcaDB.fetchAll();
        while (!marcas.isAfterLast()){
            listMarca.add(new MarcaModel(marcas.getInt(0), marcas.getString(1)));
            marcas.moveToNext();
        }
        marcaDB.close();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        marcaRecyclerView.setLayoutManager(mLayoutManager);
        MarcaAdapter categoriesAdapter = new MarcaAdapter(listMarca, this.getContext());
        marcaRecyclerView.setAdapter(categoriesAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<MarcaModel> listMarca = new ArrayList<MarcaModel>();
        RecyclerView marcaRecyclerView = getView().findViewById(R.id.marcaRecyclerView);
        MarcaDB marcaDB = new MarcaDB(this.getContext()).open();
        Cursor marcas = marcaDB.fetchAll();
        while (!marcas.isAfterLast()){
            listMarca.add(new MarcaModel(marcas.getInt(0), marcas.getString(1)));
            marcas.moveToNext();
        }
        marcaDB.close();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        marcaRecyclerView.setLayoutManager(mLayoutManager);
        MarcaAdapter categoriesAdapter = new MarcaAdapter(listMarca, this.getContext());
        marcaRecyclerView.setAdapter(categoriesAdapter);
    }
}