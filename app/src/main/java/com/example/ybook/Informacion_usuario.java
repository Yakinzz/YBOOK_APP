package com.example.ybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Informacion_usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_usuario);

        EditText nombre = findViewById(R.id.txtNombreUsuario);
        EditText apellidos = findViewById(R.id.txtApellidos);
        EditText nacionalidad = findViewById(R.id.txtNacionalidad);
        EditText email = findViewById(R.id.txtEmailUsuario);
        EditText genero = findViewById(R.id.txtGenero);
        EditText usernameCajaTexto = findViewById(R.id.txtUsername);
        EditText telefono = findViewById(R.id.txtTelefono);
        EditText fechaNacimiento = findViewById(R.id.txtFechaNacimientoUsuario);

        Intent intent = getIntent();
        String usernameOriginal = intent.getStringExtra("username");

        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios");

                while(rs.next()){
                    String usernameBBDD = rs.getString(9);
                    if(usernameOriginal.equals(usernameBBDD)){
                        usernameCajaTexto.setText(usernameBBDD);
                        nombre.setText(rs.getString(2));
                        apellidos.setText(rs.getString(3));
                        nacionalidad.setText(rs.getString(7));
                        email.setText(rs.getString(4));
                        genero.setText(rs.getString(6));
                        telefono.setText(rs.getString(8));
                        fechaNacimiento.setText(rs.getString(5));
                    }
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }


        Button editarUsuario = findViewById(R.id.btnEditarInfoUsuario);
        editarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre.setClickable(true);
                nombre.setFocusable(true);
                nombre.setActivated(true);
                nombre.setEnabled(true);
                nombre.setFocusableInTouchMode(true);
                editarUsuario.setVisibility(View.GONE);
            }
        });


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
}