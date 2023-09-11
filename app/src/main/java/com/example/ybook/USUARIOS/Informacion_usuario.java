package com.example.ybook.USUARIOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.LIBROS.Informacion_libro;
import com.example.ybook.PaginaPrincipal;
import com.example.ybook.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Informacion_usuario extends AppCompatActivity {

    TextView nombreUsuario;
    TextView apellidosUsuario;
    TextView nacionalidadUsuario;
    TextView emailUsuario;
    TextView usernameUsuario;
    TextView generoUsuario;
    TextView telefonoUsuario;
    TextView fechaNacimientoUsuario;

    Button editarUsuario;
    Button cancelarCambios;
    Button aceptarCambios;
    Button eliminarUsuario;

    int idUsuario = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_usuario);

        nombreUsuario = findViewById(R.id.txtNombreUsuario);
        apellidosUsuario= findViewById(R.id.txtApellidos);
        nacionalidadUsuario= findViewById(R.id.txtNacionalidad);
        emailUsuario = findViewById(R.id.txtEmailUsuario);
        usernameUsuario = findViewById(R.id.txtUsername);
        generoUsuario = findViewById(R.id.txtGenero);
        telefonoUsuario = findViewById(R.id.txtTelefono);
        fechaNacimientoUsuario= findViewById(R.id.txtFechaNacimientoUsuario);

        editarUsuario= findViewById(R.id.btnEditarInfoUsuario);
        cancelarCambios = findViewById(R.id.btnCancelarCambios);
        aceptarCambios = findViewById(R.id.btnActualizarCambios);
        eliminarUsuario = findViewById(R.id.btnEliminarUsuario);

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
                        idUsuario=rs.getInt(1);
                        usernameUsuario.setText(usernameBBDD);
                        nombreUsuario.setText(rs.getString(2));
                        apellidosUsuario.setText(rs.getString(3));
                        emailUsuario.setText(rs.getString(4));
                        fechaNacimientoUsuario.setText(rs.getString(5));
                        generoUsuario.setText(rs.getString(6));
                        nacionalidadUsuario.setText(rs.getString(7));
                        telefonoUsuario.setText(rs.getString(8));

                    }
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }



        editarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambioVisibilidad(true);
                editarUsuario.setVisibility(View.GONE);
                aceptarCambios.setVisibility(View.VISIBLE);
                cancelarCambios.setVisibility(View.VISIBLE);
            }
        });

        cancelarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambioVisibilidad(false);
                editarUsuario.setVisibility(View.VISIBLE);
                aceptarCambios.setVisibility(View.GONE);
                cancelarCambios.setVisibility(View.GONE);
            }
        });

        eliminarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if(conn!=null){


                        PreparedStatement stm4 = conexionBD().prepareStatement("DELETE FROM Roles WHERE ID_Usuario="+idUsuario);
                        stm4.executeUpdate();

                        PreparedStatement stm3 = conexionBD().prepareStatement("DELETE FROM Valoraciones WHERE UsuarioID="+idUsuario);
                        stm3.executeUpdate();

                        PreparedStatement stm2 = conexionBD().prepareStatement("DELETE FROM MisLibros WHERE ID_Usuario="+idUsuario);
                        stm2.executeUpdate();

                        PreparedStatement stm = conexionBD().prepareStatement("DELETE FROM Usuarios WHERE UsuarioID="+idUsuario);
                        stm.executeUpdate();

                        Toast.makeText(Informacion_usuario.this, "USUARIO ELIMINADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                startActivity(intent2);
                                finish();
                            }
                        },2000);

                    }else{
                        Toast.makeText(Informacion_usuario.this,"Error en la conexion", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"ERROR EN LA ELIMINACIÓN DEL LIBRO",Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }

            }

        });


    }

    private void cambioVisibilidad(Boolean a){
        nombreUsuario.setClickable(a);
        nombreUsuario.setFocusable(a);
        nombreUsuario.setEnabled(a);
        nombreUsuario.setFocusableInTouchMode(a);

        apellidosUsuario.setClickable(a);
        apellidosUsuario.setFocusable(a);
        apellidosUsuario.setEnabled(a);
        apellidosUsuario.setFocusableInTouchMode(a);

        nacionalidadUsuario.setClickable(a);
        nacionalidadUsuario.setFocusable(a);
        nacionalidadUsuario.setEnabled(a);
        nacionalidadUsuario.setFocusableInTouchMode(a);

        emailUsuario.setClickable(a);
        emailUsuario.setFocusable(a);
        emailUsuario.setEnabled(a);
        emailUsuario.setFocusableInTouchMode(a);

        usernameUsuario.setClickable(a);
        usernameUsuario.setFocusable(a);
        usernameUsuario.setEnabled(a);
        usernameUsuario.setFocusableInTouchMode(a);

        generoUsuario.setClickable(a);
        generoUsuario.setFocusable(a);
        generoUsuario.setEnabled(a);
        generoUsuario.setFocusableInTouchMode(a);

        telefonoUsuario.setClickable(a);
        telefonoUsuario.setFocusable(a);
        telefonoUsuario.setActivated(a);
        telefonoUsuario.setEnabled(a);

        fechaNacimientoUsuario.setClickable(a);
        fechaNacimientoUsuario.setFocusable(a);
        fechaNacimientoUsuario.setActivated(a);
        fechaNacimientoUsuario.setEnabled(a);


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