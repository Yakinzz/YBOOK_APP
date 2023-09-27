package com.example.ybook.USUARIOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.Entidades.Libro;
import com.example.ybook.Entidades.Usuario;
import com.example.ybook.LIBROS.Informacion_libro;
import com.example.ybook.LIBROS.libros;
import com.example.ybook.PaginaPrincipal;
import com.example.ybook.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class Informacion_usuario extends AppCompatActivity {

    Usuario usuarioBBDD;

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
    Button resetPassword;

    int idUsuario = 0;
    int idUsuarioOriginal =0;



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
        resetPassword = findViewById(R.id.btnResetearPasswordUsuario);


        Intent intent = getIntent();
        String usernameOriginal = intent.getStringExtra("username");
        idUsuarioOriginal = intent.getIntExtra("id",0);



        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE Username='"+usernameOriginal+"'");

                if(rs.next()){
                    usuarioBBDD=new Usuario(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getString(9),rs.getString(10),rs.getString(11));
                    idUsuario=rs.getInt(1);
                    nombreUsuario.setText(rs.getString(2));
                    apellidosUsuario.setText(rs.getString(3));
                    emailUsuario.setText(rs.getString(4));
                    fechaNacimientoUsuario.setText(rs.getString(5));
                    generoUsuario.setText(rs.getString(6));
                    nacionalidadUsuario.setText(rs.getString(7));
                    telefonoUsuario.setText(String.valueOf(usuarioBBDD.getTelefono()));
                    usernameUsuario.setText(usuarioBBDD.getUsername());

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

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(conn!=null){
                        String passwordDefault="Holamundo";
                        PreparedStatement stm = conexionBD().prepareStatement("UPDATE Usuarios SET Password='"+ passwordDefault +"' WHERE UsuarioID='"+ idUsuarioOriginal +"'");
                        stm.executeUpdate();

                        Toast.makeText(Informacion_usuario.this, "CONTRASEÑA DEL USUARIO RESETEADA", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                        startActivity(intent2);

                    }

                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }
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

        aceptarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idActualizar = idUsuario;
                String nombreActualizar = null;
                String apellidosActualizar = null;
                String emailActualizar= null;
                String fechaNacimientoActualizar= null;
                String generoActualizar= null;
                String nacionalidadActualizar= null;
                int telefonoActualizar = 0;
                String usernameActualizar = null;
                String passwordActualizar = null;


                //Validación del nombre
                if(!(nombreUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getNombre().equals(nombreUsuario.getText().toString())){
                        nombreActualizar =usuarioBBDD.getNombre();
                    }else{
                        nombreActualizar =(String)nombreUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de los apellidos
                if(!(apellidosUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getApellidos().equals(apellidosUsuario.getText().toString())){
                        apellidosActualizar =usuarioBBDD.getApellidos();
                    }else{
                        apellidosActualizar =(String)apellidosUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "Los apelldios no pueden quedar vacios", Toast.LENGTH_SHORT).show();
                }

                //Validación del email
                if(!(emailUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getEmail().equals(emailUsuario.getText().toString())){
                        emailActualizar =usuarioBBDD.getEmail();
                    }else{
                        if(Patterns.EMAIL_ADDRESS.matcher(emailUsuario.getText()).matches()){
                            emailActualizar=(String)emailUsuario.getText().toString();
                            Toast.makeText(Informacion_usuario.this, "EMAIL CORRECTO", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(Informacion_usuario.this, "El email no tiene el formato correcto", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "El email no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la fecha de nacimiento
                if(!(fechaNacimientoUsuario.getText().toString().isEmpty())){
                    if(usuarioBBDD.getFechaNacimiento().equals(fechaNacimientoUsuario.getText().toString())){
                        fechaNacimientoActualizar =usuarioBBDD.getFechaNacimiento();
                    }else{
                        String formatoFecha = "yyyy/MM/dd";
                        String fechafecha =(String) fechaNacimientoUsuario.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                        sdf.setLenient(false);

                        try {
                            Date fecha = sdf.parse(fechafecha);
                            fechaNacimientoActualizar = fechafecha;
                        }catch (ParseException e) {
                            Toast.makeText(Informacion_usuario.this, "La fecha ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(Informacion_usuario.this, "La fecha de publicación no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del genero
                if(!(generoUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getGenero().equals(generoUsuario.getText().toString())){
                        generoActualizar =usuarioBBDD.getGenero();
                    }else{
                        generoActualizar =(String)generoUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "El género no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la nacionalidad
                if(!(nacionalidadUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getNacionalidad().equals(nacionalidadUsuario.getText().toString())){
                        nacionalidadActualizar =usuarioBBDD.getNacionalidad();
                    }else{
                        nacionalidadActualizar =(String)nacionalidadUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "La nacionalidad no puede quedar vacía", Toast.LENGTH_SHORT).show();
                }

                //Validación del username
                if(!(usernameUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getUsername().equals(usernameUsuario.getText().toString())){
                        usernameActualizar =usuarioBBDD.getUsername();
                    }else{
                        Boolean encontrado =false;
                        Connection conn = conexionBD();
                        try {
                            if(conn!=null){
                                Statement stm = conexionBD().createStatement();
                                ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios");

                                while(rs.next()){
                                    String usernameValidacion = rs.getString(9);
                                    if(usernameValidacion.equals(usernameUsuario)){
                                        encontrado=true;
                                    }
                                }
                            }

                        }catch (Exception exception){
                            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA AQUII",Toast.LENGTH_SHORT).show();
                        }

                        if(!encontrado){
                            usernameActualizar =(String)usernameUsuario.getText().toString();
                        }else{
                            Toast.makeText(getApplicationContext(),"El username ya se encuentra dado de alta",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "El username no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación del telefono
                if(!(telefonoUsuario.getText().toString().isEmpty())){
                    int contenidoCajaTelefono = Integer.parseInt((String) telefonoUsuario.getText().toString());
                    if(usuarioBBDD.getTelefono() == contenidoCajaTelefono){
                        telefonoActualizar =usuarioBBDD.getTelefono();
                    }else{
                        telefonoActualizar = Integer.parseInt((String) telefonoUsuario.getText().toString());
                    }

                }else{
                    Toast.makeText(Informacion_usuario.this, "El teléfono no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                if(nombreActualizar!=null && apellidosActualizar!=null && nacionalidadActualizar!=null && emailActualizar!=null && usernameActualizar!=null && generoActualizar!=null && telefonoActualizar!=0 && fechaNacimientoActualizar!=null){

                    try {
                        if(conn!=null){

                            PreparedStatement stm = conexionBD().prepareStatement("UPDATE Usuarios SET Nombre='"+ nombreActualizar +"',Apellidos='"+ apellidosActualizar +"',Nacionalidad='"+ nacionalidadActualizar +"',Email='"+ emailActualizar +"',Username='"+ usernameActualizar +"',Genero='"+ generoActualizar +"',Telefono='"+ telefonoActualizar +"',FechaNacimiento='"+ fechaNacimientoActualizar +"' WHERE UsuarioID='"+ usuarioBBDD.getId() +"'");
                            stm.executeUpdate();

                            Toast.makeText(Informacion_usuario.this, "DATOS USUARIO ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                            startActivity(intent2);

                        }

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"NO ESTÁ TODO BIEN",Toast.LENGTH_SHORT).show();
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