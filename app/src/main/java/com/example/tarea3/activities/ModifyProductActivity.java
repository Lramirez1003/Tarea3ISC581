package com.example.tarea3.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tarea3.R;
import com.example.tarea3.fragments.CreateUpdateProductFragment;

public class ModifyProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Integer id = getIntent().getExtras().getInt("id");
        setContentView(R.layout.activity_create_product);
        CreateUpdateProductFragment fragment = CreateUpdateProductFragment.newInstance(true, id);
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmento,fragment);
        fragmentTransaction.commit();
    }
}