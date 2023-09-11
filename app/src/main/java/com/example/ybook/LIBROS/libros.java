package com.example.ybook.LIBROS;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ybook.R;
import com.example.ybook.adapters.AdaptadorLibros;
import com.example.ybook.models.ListElementLibros;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class libros extends AppCompatActivity implements AdaptadorLibros.OnLibroListener {
    List<ListElementLibros> elements;
    int idLibro=0;
    FloatingActionButton addLibro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libros);

        init();

        addLibro=findViewById(R.id.floatingActionButton);
        addLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(libros.this, Add_NuevoLibro.class);
                startActivity(intent);
            }
        });
    }

    public  void  init(){
        elements = new ArrayList<>();


        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Libros");

                Boolean encontrado = false;

                while(rs.next()){
                    String titulo = rs.getString(2);
                    String editorial = rs.getString(5);
                    idLibro=rs.getInt(1);
                    int idAutor = rs.getInt(3);


                    try {
                        if(conn!=null){
                            Statement stm2 = conexionBD().createStatement();
                            ResultSet rs2 = stm2.executeQuery("SELECT * FROM Autores");

                            if(rs2.next()){
                                int id = rs2.getInt(1);
                                if(id==idAutor){
                                    encontrado=true;
                                }
                            }
                        }

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA DEL AUTOR",Toast.LENGTH_SHORT).show();
                    }

                    if(encontrado){
                        elements.add(new ListElementLibros(titulo,editorial,idAutor));
                    }

                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }

        AdaptadorLibros listAdapter = new AdaptadorLibros(elements,this,this);
        RecyclerView recyclerView = findViewById(R.id.listaLibros);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

    }

    public static java.sql.Connection conexionBD(){
        java.sql.Connection conexion = null;
        try {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.135;databaseName=YBOOK;user=sa;password=root;");
        }catch(Exception e){
            Log.e("Error de conexion",e.getMessage());
        }
        return  conexion;
    }

    @Override
    public void onLibroClick(int position) {
        Log.d(TAG, "onLibroClick: clicked");

        ListElementLibros elemento = elements.get(position);

        Intent intent = new Intent(this, Informacion_libro.class);
        intent.putExtra("titulo",elemento.getTitulo());
        intent.putExtra("idAutor",elemento.getAutor());
        intent.putExtra("idLibro",idLibro);
        Log.d(TAG, "onLibroClick: titulo" + elemento.getTitulo());
        startActivity(intent);

    }
}