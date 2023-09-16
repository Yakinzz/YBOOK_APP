package com.example.ybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.Entidades.Usuario;
import com.example.ybook.USUARIOS.Informacion_usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarPerfil extends AppCompatActivity {

    int idUsuario = 0;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

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


        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("ID",0);

        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE UsuarioID="+idUsuario);

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
                    Toast.makeText(EditarPerfil.this, "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de los apellidos
                if(!(apellidosUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getApellidos().equals(apellidosUsuario.getText().toString())){
                        apellidosActualizar =usuarioBBDD.getApellidos();
                    }else{
                        apellidosActualizar =(String)apellidosUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(EditarPerfil.this, "Los apelldios no pueden quedar vacios", Toast.LENGTH_SHORT).show();
                }

                //Validación del email
                if(!(emailUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getEmail().equals(emailUsuario.getText().toString())){
                        emailActualizar =usuarioBBDD.getEmail();
                    }else{
                        if(Patterns.EMAIL_ADDRESS.matcher(emailUsuario.getText()).matches()){
                            emailActualizar=(String)emailUsuario.getText().toString();
                            Toast.makeText(EditarPerfil.this, "EMAIL CORRECTO", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(EditarPerfil.this, "El email no tiene el formato correcto", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    Toast.makeText(EditarPerfil.this, "El email no puede quedar vacio", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditarPerfil.this, "La fecha ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(EditarPerfil.this, "La fecha de publicación no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del genero
                if(!(generoUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getGenero().equals(generoUsuario.getText().toString())){
                        generoActualizar =usuarioBBDD.getGenero();
                    }else{
                        generoActualizar =(String)generoUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(EditarPerfil.this, "El género no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la nacionalidad
                if(!(nacionalidadUsuario.getText().toString().isEmpty())){

                    if(usuarioBBDD.getNacionalidad().equals(nacionalidadUsuario.getText().toString())){
                        nacionalidadActualizar =usuarioBBDD.getNacionalidad();
                    }else{
                        nacionalidadActualizar =(String)nacionalidadUsuario.getText().toString();
                    }

                }else{
                    Toast.makeText(EditarPerfil.this, "La nacionalidad no puede quedar vacía", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarPerfil.this, "El username no puede quedar vacio", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarPerfil.this, "El teléfono no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                if(nombreActualizar!=null && apellidosActualizar!=null && nacionalidadActualizar!=null && emailActualizar!=null && usernameActualizar!=null && generoActualizar!=null && telefonoActualizar!=0 && fechaNacimientoActualizar!=null){

                    try {
                        if(conn!=null){

                            PreparedStatement stm = conexionBD().prepareStatement("UPDATE Usuarios SET Nombre='"+ nombreActualizar +"',Apellidos='"+ apellidosActualizar +"',Nacionalidad='"+ nacionalidadActualizar +"',Email='"+ emailActualizar +"',Username='"+ usernameActualizar +"',Genero='"+ generoActualizar +"',Telefono='"+ telefonoActualizar +"',FechaNacimiento='"+ fechaNacimientoActualizar +"' WHERE UsuarioID='"+ usuarioBBDD.getId() +"'");
                            stm.executeUpdate();

                            Toast.makeText(EditarPerfil.this, "DATOS USUARIO ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                            intent2.putExtra("ID",idUsuario);
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