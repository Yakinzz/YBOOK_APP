package com.example.ybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class PaginaPrincipal extends AppCompatActivity implements View.OnClickListener{

    private CardView libros,usuarios,autores,ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pagina_principal);
        libros = (CardView) findViewById(R.id.Libros);
        usuarios = (CardView) findViewById(R.id.Usuarios);
        autores = (CardView) findViewById(R.id.Autores);
        ajustes = (CardView) findViewById(R.id.Ajustes);

        libros.setOnClickListener((View.OnClickListener) this);
        usuarios.setOnClickListener((View.OnClickListener) this);
        autores.setOnClickListener((View.OnClickListener) this);
        ajustes.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public void onClick(View v){
        Intent i;
        int id = v.getId();
        if(id == R.id.Libros){
            i=new Intent(this, com.example.ybook.LIBROS.libros.class); startActivity(i);
        } else if (id == R.id.Usuarios) {
            i=new Intent(this, com.example.ybook.USUARIOS.usuarios.class); startActivity(i);
        }else if (id == R.id.Autores) {
            i=new Intent(this, com.example.ybook.AUTORES.autores.class); startActivity(i);
        }else if (id == R.id.Ajustes) {
            i=new Intent(this, com.example.ybook.AJUSTES.ajustes.class); startActivity(i);
        }

    }

}