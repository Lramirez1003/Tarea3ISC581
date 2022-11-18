package com.example.tarea3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tarea3.R;
import com.example.tarea3.dbModels.MarcaDB;


public class CreateMarcaFragment extends Fragment {

    public CreateMarcaFragment() {}


    public static CreateMarcaFragment newInstance() {
        CreateMarcaFragment fragment = new CreateMarcaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Agregar Marca");
        View view =  inflater.inflate(R.layout.fragment_create_marca, container, false);
        Button btnSave = view.findViewById(R.id.btnSaveMarca);
        EditText name = view.findViewById(R.id.marcaNameEditText);
        btnSave.setOnClickListener(v -> {
            if(!name.getText().toString().isEmpty()) {
                MarcaDB marcaDB = new MarcaDB(getContext()).open();
                marcaDB.insert(name.getText().toString());
                marcaDB.close();
                this.getActivity().finish();
            }else {
                Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}