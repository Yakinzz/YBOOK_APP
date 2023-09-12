package com.example.ybook.USUARIOS;

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
import com.example.ybook.adapters.AdaptadorUsuarios;
import com.example.ybook.models.ListElementUsuarios;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class usuarios extends AppCompatActivity implements AdaptadorUsuarios.OnUsuariosListener {

    List<ListElementUsuarios> elements;
    FloatingActionButton btnAddUsuario;
    int idUsuario=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        init();

        btnAddUsuario =findViewById(R.id.btnNuevoUsuario);
        btnAddUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(usuarios.this, Add_NuevoUsuario.class);
                //intent.putExtra("username",elemento.getUsername());
                startActivity(intent2);
            }
        });
    }

    public  void  init(){
        elements = new ArrayList<>();

        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE ROL='Usuario'");

                while(rs.next()){
                    idUsuario = rs.getInt(1);
                    String nombre = rs.getString(2);
                    String apellidos = rs.getString(3);
                    String email = rs.getString(4);
                    String fechaNacimiento =rs.getString(5);
                    String username =rs.getString(9);

                    elements.add(new ListElementUsuarios(username,email));
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }

        AdaptadorUsuarios listAdapter = new AdaptadorUsuarios(elements,this,this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsuarios);
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
    public void onUsuarioClick(int position) {
        Log.d(TAG, "onUsuarioClick: clicked");

        ListElementUsuarios elemento = elements.get(position);

        Intent intent = new Intent(this, Informacion_usuario.class);
        intent.putExtra("username",elemento.getUsername());
        intent.putExtra("id",idUsuario);
        //Log.d(TAG, "onLibroClick: titulo" + elemento.no());
        startActivity(intent);
    }

}