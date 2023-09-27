package com.example.ybook.USUARIOS;

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

import com.example.ybook.AUTORES.Add_NuevoAutor;
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

public class Add_NuevoUsuario extends AppCompatActivity {

    Button btnCrearUsuario;
    TextView nombreUsuario;
    TextView apellidosUsuario;
    TextView emailUsuario;
    TextView telefonoUsuario;
    TextView nacionalidadUsuario;
    TextView generoUsuario;
    TextView fechaNacimientoUsuario;
    TextView usernameUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nuevo_usuario);

        btnCrearUsuario=(Button) findViewById(R.id.btnCrearNuevoUsuario);

        nombreUsuario = findViewById(R.id.txtNombre2);
        apellidosUsuario = findViewById(R.id.txtApellidos2);
        emailUsuario = findViewById(R.id.txtEmail2);
        telefonoUsuario = findViewById(R.id.txtTelefono2);
        nacionalidadUsuario = findViewById(R.id.txtNacionalidad2);
        generoUsuario = findViewById(R.id.txtGenero2);
        fechaNacimientoUsuario = findViewById(R.id.txtFechaNacimiento2);
        usernameUsuario = findViewById(R.id.txtUsername2);

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombreNuevo = null;
                String apellidosNuevo = null;
                String emailNuevo = null;
                int telefonoNuevo = 0;
                String nacionalidadNuevo = null;
                String generoNuevo = null;
                String fechaNacimientoNuevo= null;
                String usernameNuevo= null;

                //Validación del nombre
                if(!(nombreUsuario.getText().toString().isEmpty())){
                    nombreNuevo =(String)nombreUsuario.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de los apellidos
                if(!(apellidosUsuario.getText().toString().isEmpty())){
                    apellidosNuevo =(String)apellidosUsuario.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "Los apellidos no pueden quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación del email
                if(!(emailUsuario.getText().toString().isEmpty())){
                    if(Patterns.EMAIL_ADDRESS.matcher(emailUsuario.getText()).matches()){
                        emailNuevo=(String)emailUsuario.getText().toString();


                    }else{
                        Toast.makeText(Add_NuevoUsuario.this, "El email no tiene el formato correcto", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "El email no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación del telefono
                if(!(telefonoUsuario.getText().toString().isEmpty())){
                    telefonoNuevo = Integer.parseInt((String) telefonoUsuario.getText().toString());
                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "El teléfono no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la nacionalidad
                if(!(nacionalidadUsuario.getText().toString().isEmpty())){
                    nacionalidadNuevo =(String)nacionalidadUsuario.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "La nacionalidad no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del genero
                if(!(generoUsuario.getText().toString().isEmpty())){
                    generoNuevo =(String)generoUsuario.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "El genero no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la fecha de nacimiento
                if(!(fechaNacimientoUsuario.getText().toString().isEmpty())){
                    String formatoFecha = "yyyy/MM/dd";
                    String fechafecha =(String) fechaNacimientoUsuario.getText().toString();

                    SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                    sdf.setLenient(false);

                    try {
                        Date fecha = sdf.parse(fechafecha);
                        fechaNacimientoNuevo = fechafecha;
                    }catch (ParseException e) {
                        Toast.makeText(Add_NuevoUsuario.this, "La fecha de fallecimiento ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "La fecha de fallecimiento no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del username
                if(!(usernameUsuario.getText().toString().isEmpty())){
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
                            usernameNuevo =(String)usernameUsuario.getText().toString();
                        }else{
                            Toast.makeText(getApplicationContext(),"El username ya se encuentra dado de alta",Toast.LENGTH_SHORT).show();
                        }


                }else{
                    Toast.makeText(Add_NuevoUsuario.this, "El username no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                if(nombreNuevo!=null && apellidosNuevo!=null && emailNuevo!=null && nacionalidadNuevo!=null && fechaNacimientoNuevo!=null && generoNuevo!=null && telefonoNuevo!=0 && usernameNuevo!=null){

                    try {
                        Connection conn = conexionBD();
                        if(conn!=null){
                            String passwordDefault = "Holamundo";
                            String rolDefault = "Usuario";
                            PreparedStatement stm = conexionBD().prepareStatement("INSERT INTO Usuarios (Nombre,Apellidos,Email,FechaNacimiento,Genero,Nacionalidad,Telefono,Username,Password,Rol) VALUES ('"+nombreNuevo+"','"+apellidosNuevo+"','"+emailNuevo+"','"+fechaNacimientoNuevo+"','"+generoNuevo+"','"+nacionalidadNuevo+"','"+telefonoNuevo+"','"+usernameNuevo+"','"+passwordDefault+"','"+rolDefault+"')");
                            stm.executeUpdate();

                            Toast.makeText(Add_NuevoUsuario.this, "USUARIO CREADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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