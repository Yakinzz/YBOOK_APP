package com.example.ybook;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ybook.adapters.AdaptadorAutores;
import com.example.ybook.adapters.MyAdapter;
import com.example.ybook.models.ListElementAutores;
import com.example.ybook.models.ListElementLibros;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class autores extends AppCompatActivity implements AdaptadorAutores.OnAutorListener {

    List<ListElementAutores> elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autores);
        init();
    }

    public  void  init(){
        elements = new ArrayList<>();


        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Autores");

                while(rs.next()){
                    String nombre = rs.getString(2);
                    String apellidos = rs.getString(3);
                    String nacionalidad = rs.getString(4);
                    String fechaNacimiento =rs.getString(5);
                    String fechaFallecimiento =rs.getString(6);

                    elements.add(new ListElementAutores(nombre));
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }

        AdaptadorAutores listAdapter = new AdaptadorAutores(elements,this,this);
        RecyclerView recyclerView = findViewById(R.id.listaautores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
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
    public void onAutorClick(int position) {
        Log.d(TAG, "onAutorClick: clicked");

        ListElementAutores elemento = elements.get(position);

        Intent intent = new Intent(this,Informacion_autores.class);
        intent.putExtra("titulo",elemento.getNombreAutor());
        //Log.d(TAG, "onLibroClick: titulo" + elemento.no());
        startActivity(intent);
    }
}