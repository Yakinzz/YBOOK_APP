package com.example.ybook.AUTORES;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.Entidades.Autor;
import com.example.ybook.LIBROS.Add_NuevoLibro;
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

public class Add_NuevoAutor extends AppCompatActivity {

    Button btnAddNuevoAutor;

    Autor autorBBDD;

    TextView nombreAutor;
    TextView apellidosAutor;
    TextView nacionalidadAutor;
    TextView fechaNacimientoAutor;
    TextView fechaFallecimientoAutor;

    TextView labelFallecido;
    RadioButton rb_si;
    RadioButton rb_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nuevo_autor);

        nombreAutor = findViewById(R.id.txtNombreNuevoAutor);
        apellidosAutor = findViewById(R.id.txtApellidosNuevoAutor);
        nacionalidadAutor = findViewById(R.id.txtNacionalidadNuevoAutor);
        fechaNacimientoAutor = findViewById(R.id.txtFechaNacimientoNuevoAutor);
        fechaFallecimientoAutor = findViewById(R.id.txtFechaFallecimientoNuevoAutor);
        btnAddNuevoAutor = findViewById(R.id.btnNuevoAutor);

        rb_si= findViewById(R.id.rb_fallecidoSInuevo);
        rb_no = findViewById(R.id.rb_fallecidoNOnuevo);
        labelFallecido = findViewById(R.id.lb_FallecidoNuevo);

        rb_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_si.isChecked()) {
                    labelFallecido.setVisibility(View.VISIBLE);
                    fechaFallecimientoAutor.setVisibility(View.VISIBLE);
                }
            }
        });

        rb_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_no.isChecked()) {
                    labelFallecido.setVisibility(View.GONE);
                    fechaFallecimientoAutor.setVisibility(View.GONE);
                }
            }
        });


        btnAddNuevoAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreNuevo = null;
                String apellidosNuevo = null;
                String nacionalidadNuevo = null;
                String fechaNacimientoNuevo = null;
                String fechaFallecimientoNuevo= null;

                //Validación del nombre
                if(!(nombreAutor.getText().toString().isEmpty())){
                    nombreNuevo =(String)nombreAutor.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoAutor.this, "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de los apellidos
                if(!(apellidosAutor.getText().toString().isEmpty())){
                    apellidosNuevo =(String)apellidosAutor.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoAutor.this, "Los apellidos no pueden quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la nacionalidad
                if(!(nacionalidadAutor.getText().toString().isEmpty())){
                        nacionalidadNuevo =(String)nacionalidadAutor.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoAutor.this, "La nacionalidad no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación de la fecha de nacimiento
                if(!(fechaNacimientoAutor.getText().toString().isEmpty())){
                        String formatoFecha = "yyyy/MM/dd";
                        String fechafecha =(String) fechaNacimientoAutor.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                        sdf.setLenient(false);

                        try {
                            Date fecha = sdf.parse(fechafecha);
                            fechaNacimientoNuevo = fechafecha;
                        }catch (ParseException e) {
                            Toast.makeText(Add_NuevoAutor.this, "La fecha de nacimiento ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                        }

                }else{
                    Toast.makeText(Add_NuevoAutor.this, "La fecha de nacimiento no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }





                if(nombreNuevo!=null && apellidosNuevo!=null && nacionalidadNuevo!=null && fechaNacimientoNuevo!=null){
                    int contador=0;
                    try {

                        Connection conn = conexionBD();
                        if(conn!=null){
                            Statement stm = conexionBD().createStatement();
                            ResultSet rs = stm.executeQuery("SELECT * FROM Autores");

                            while(rs.next()){
                                if((nombreNuevo.equals(rs.getString(2))) && (apellidosNuevo.equals(rs.getString(3)))){
                                    Toast.makeText(getApplicationContext(),"Este autor ya existe.",Toast.LENGTH_SHORT).show();
                                    contador++;
                                }
                            }
                        }

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA 1",Toast.LENGTH_SHORT).show();
                    }

                    if(contador == 0){
                        if(rb_si.isChecked()){
                            if(!(fechaFallecimientoAutor.getText().toString().isEmpty())){
                                String formatoFecha = "yyyy/MM/dd";
                                String fechafecha =(String) fechaFallecimientoAutor.getText().toString();

                                SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                                sdf.setLenient(false);

                                try {
                                    Date fecha = sdf.parse(fechafecha);
                                    fechaFallecimientoNuevo = fechafecha;
                                }catch (ParseException e) {
                                    Toast.makeText(Add_NuevoAutor.this, "La fecha de fallecimiento ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(Add_NuevoAutor.this, "La fecha de fallecimiento no puede quedar vacia", Toast.LENGTH_SHORT).show();
                            }

                            if(fechaFallecimientoNuevo!=null){
                                try {
                                    Connection conn = conexionBD();
                                    if(conn!=null){
                                        //nombreNuevo!=null && apellidosNuevo!=null && nacionalidadNuevo!=null && fechaNacimientoNuevo!=null
                                        PreparedStatement stm = conexionBD().prepareStatement("INSERT INTO Autores (Nombre,Apellidos,Nacionalidad,FechaNacimiento,FechaFallecimiento) VALUES ('"+nombreNuevo+"','"+apellidosNuevo+"','"+nacionalidadNuevo+"','"+fechaNacimientoNuevo+"','"+fechaFallecimientoNuevo+"')");
                                        stm.executeUpdate();

                                        Toast.makeText(Add_NuevoAutor.this, "AUTOR CREADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                        Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                        startActivity(intent2);

                                    }

                                }catch (Exception exception){
                                    Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA2",Toast.LENGTH_SHORT).show();
                                    exception.printStackTrace();
                                }
                            }


                        } else if (rb_no.isChecked()) {
                            try {
                                Connection conn = conexionBD();
                                if(conn!=null){
                                    PreparedStatement stm = conexionBD().prepareStatement("INSERT INTO Autores (Nombre,Apellidos,Nacionalidad,FechaNacimiento) VALUES ('"+nombreNuevo+"','"+apellidosNuevo+"','"+nacionalidadNuevo+"','"+fechaNacimientoNuevo+"')");
                                    stm.executeUpdate();

                                    Toast.makeText(Add_NuevoAutor.this, "AUTOR CREADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                    startActivity(intent2);

                                }

                            }catch (Exception exception){
                                Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
                                exception.printStackTrace();
                            }

                        }
                    }



                }else{
                    Toast.makeText(getApplicationContext(),"ALGO NO VA BIEN",Toast.LENGTH_SHORT).show();
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