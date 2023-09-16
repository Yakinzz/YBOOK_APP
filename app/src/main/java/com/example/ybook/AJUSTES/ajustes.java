package com.example.ybook.AJUSTES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ybook.R;

public class ajustes extends AppCompatActivity implements View.OnClickListener {

    private CardView DatosPerfil,Privacidad,CierreSesion;
    int idUsuario=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        DatosPerfil = (CardView) findViewById(R.id.DatosPerfil);
        Privacidad = (CardView) findViewById(R.id.Privacidad);
        CierreSesion = (CardView) findViewById(R.id.CierreSesionAjustes);


        DatosPerfil.setOnClickListener((View.OnClickListener) this);
        Privacidad.setOnClickListener((View.OnClickListener) this);
        CierreSesion.setOnClickListener((View.OnClickListener) this);

        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("ID",0);
    }

    @Override
    public void onClick(View v){
        Intent i;
        int id = v.getId();
        if(id == R.id.DatosPerfil){
            i=new Intent(this, com.example.ybook.EditarPerfil.class);
            i.putExtra("ID",idUsuario);
            startActivity(i);
        } else if (id == R.id.Privacidad) {
            i=new Intent(this, com.example.ybook.Privacidad.class);
            i.putExtra("ID",idUsuario);
            startActivity(i);
        }else if (id == R.id.CierreSesionAjustes) {
            i=new Intent(this, com.example.ybook.LoginActivity.class); startActivity(i);
            Toast.makeText(this, "SE HA CERRADO LA SESIÓN", Toast.LENGTH_SHORT).show();
        }
    }

}