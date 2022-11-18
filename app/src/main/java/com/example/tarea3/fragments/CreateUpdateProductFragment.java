package com.example.tarea3.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.tarea3.R;
import com.example.tarea3.activities.CreateMarcaActivity;
import com.example.tarea3.dbModels.MarcaDB;
import com.example.tarea3.dbModels.ProductoDB;

public class CreateUpdateProductFragment extends Fragment {

    private static final String ARG_PARAM1 = "modify";
    private static final String ARG_PARAM2 = "id";
    private static int RESULT_LOAD_IMAGE = 1;

    ImageView productImage;
    EditText nombre, precio;
    Button btnUpdate, btnSave, btnDelete, btnAddMarca;
    Spinner spnMarca;

    private Boolean modify;
    private Integer id;

    public CreateUpdateProductFragment() {}


    public static CreateUpdateProductFragment newInstance(Boolean modify, Integer id) {
        CreateUpdateProductFragment fragment = new CreateUpdateProductFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, modify);
        args.putInt(ARG_PARAM2, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(getArguments());
        if (getArguments() != null) {
            modify = getArguments().getBoolean(ARG_PARAM1);
            id = getArguments().getInt(ARG_PARAM2, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_create_update_product, container, false);

        productImage = view.findViewById(R.id.productImage);
        nombre = view.findViewById(R.id.textEditName);
        precio = view.findViewById(R.id.textEditPrice);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnSave = view.findViewById(R.id.btnSave);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnAddMarca = view.findViewById(R.id.buttonAddMarca);
        spnMarca = view.findViewById(R.id.spnMarca);
        ProductoDB productoDB = new ProductoDB(this.getContext());
        MarcaDB marcaDB = new MarcaDB(this.getContext()).open();
        ArrayList<String> listMarca = new ArrayList<String>();
        Cursor marcas = marcaDB.fetchAll();
        while (!marcas.isAfterLast()){
            listMarca.add(marcas.getString(1));
            marcas.moveToNext();
        }
        productImage.setImageResource(R.drawable.cupcake);
        ArrayAdapter spnMarcaAdapter;
        spnMarcaAdapter = new ArrayAdapter<String>(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listMarca);
        spnMarca.setAdapter(spnMarcaAdapter);

        productImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)==0)
                loadImage();
            else
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        });

        btnSave.setOnClickListener(v -> {
            if(nombre.getText().toString().isEmpty() || precio.getText().toString().isEmpty() || spnMarca.getSelectedItem().toString().isEmpty())
                Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
            else{
                productoDB.open();
                marcaDB.open();
                BitmapDrawable drawable = (BitmapDrawable) productImage.getDrawable();
                Bitmap bitmap =  drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                productoDB.insert(nombre.getText().toString(),Integer.parseInt(precio.getText().toString()),
                        marcaDB.fetchByName(spnMarca.getSelectedItem().toString()).getInt(0), Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                marcaDB.close();
                productoDB.close();
                bitmap.recycle();
                this.getActivity().finish();
            }
        });

        getActivity().setTitle("Crear Producto");

        btnUpdate.setOnClickListener(v -> {
            if(nombre.getText().toString().isEmpty() || precio.getText().toString().isEmpty() || spnMarca.getSelectedItem().toString().isEmpty())
                Toast.makeText(getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
            else{
                productoDB.open();
                marcaDB.open();
                BitmapDrawable drawable = (BitmapDrawable) productImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                productoDB.update(id, nombre.getText().toString(),Integer.parseInt(precio.getText().toString()),
                        marcaDB.fetchByName(spnMarca.getSelectedItem().toString()).getInt(0), Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                marcaDB.close();
                productoDB.close();
                bitmap.recycle();
                this.getActivity().finish();
            }
        });

        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Borrar producto");
            alertDialogBuilder.setMessage("Está seguro de que quiere borrar este producto?");
            alertDialogBuilder.setPositiveButton("Sí", (dialogInterface, i) -> {
                productoDB.open();
                productoDB.delete(id);
                productoDB.close();
                CreateUpdateProductFragment.this.getActivity().finish();
            });
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.show();

        });

        btnAddMarca.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateMarcaActivity.class);
            startActivity(intent);
        });

        if (modify) {
            getActivity().setTitle("Modificar Producto");
            productoDB.open();
            Cursor cursor = productoDB.fetchByID(id);
            btnSave.setVisibility(View.GONE);
            nombre.setText(cursor.getString(1));
            precio.setText(String.valueOf(cursor.getInt(2)));
            byte[] valueDecoded= new byte[0];
            try {
                valueDecoded = Base64.decode(cursor.getString(4), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            productImage.setImageBitmap(BitmapFactory.decodeByteArray(valueDecoded, 0, valueDecoded.length));
            marcaDB.open();
            String catName = marcaDB.fetchByID(cursor.getInt(3)).getString(1);
            marcaDB.close();
            for (int i= 0; i < spnMarca.getAdapter().getCount(); i++)
                if(spnMarca.getAdapter().getItem(i).toString().equals(catName))
                    spnMarca.setSelection(i);
        } else{
            btnUpdate.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Spinner spnMarca = getView().findViewById(R.id.spnMarca);
        MarcaDB marcaDB = new MarcaDB(this.getContext()).open();
        List<String> listMarca = new ArrayList<>();
        Cursor marcas = marcaDB.fetchAll();
        while (!marcas.isAfterLast()){
            listMarca.add(marcas.getString(1));
            marcas.moveToNext();
        }
        ArrayAdapter<String> spnMarcaAdapter = new ArrayAdapter<String>(this.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listMarca);
        spnMarca.setAdapter(spnMarcaAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 200 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            loadImage();
        else
            Toast.makeText(this.getContext(), "Necessary permissions not granted.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("requestCode", Integer.toString(requestCode));
        Log.wtf("resultCode", Integer.toString(resultCode));
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            try {
                final Uri imagen = data.getData();
                productImage = getView().findViewById(R.id.productImage);
                productImage.setImageURI(imagen);
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadImage() {
        Intent storageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        storageIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(storageIntent, "Select Picture"), RESULT_LOAD_IMAGE);

    }
}