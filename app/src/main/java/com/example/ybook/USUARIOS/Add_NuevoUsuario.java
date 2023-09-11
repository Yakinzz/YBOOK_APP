package com.example.ybook.USUARIOS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.ybook.R;

public class Add_NuevoUsuario extends AppCompatActivity {

    Button btnCrearUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nuevo_usuario);

        btnCrearUsuario=(Button) findViewById(R.id.btnNuevoAutor);
    }
}