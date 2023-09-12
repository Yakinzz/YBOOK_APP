package com.example.ybook.AUTORES;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.Entidades.Autor;
import com.example.ybook.Entidades.Libro;
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

public class Informacion_autores extends AppCompatActivity {

    Autor autorBBDD;

    int idAutorClicado = 0;

    Button btnEditarInfoAutor;
    Button btnEliminarAutor;
    Button btnActualizar;
    Button btnCancelar;
    TextView nombre;
    TextView apellidos;
    TextView nacionalidad;
    TextView fechaNacimiento;
    TextView fechaFallecimiento;
    TextView labelFallecido;

    RadioButton rb_si;
    RadioButton rb_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_autores);

       btnEditarInfoAutor = findViewById(R.id.btnEditarInfoAutor);
       btnEliminarAutor = findViewById(R.id.btnEliminarAutor);
       btnActualizar = findViewById(R.id.btnActualizarCambios);
       btnCancelar = findViewById(R.id.btnCancelarCambios);
       nombre = findViewById(R.id.txtNombreAutor);
       apellidos = findViewById(R.id.txtApellidosAutor);
       nacionalidad = findViewById(R.id.txtNacionalidadAutor);
       fechaNacimiento = findViewById(R.id.txtFechaNacimientoAutor);
       fechaFallecimiento = findViewById(R.id.txtFechaFallecimientoAutor);


       rb_si= findViewById(R.id.rb_fallecidoSI);
       rb_no = findViewById(R.id.rb_fallecidoNO);
       labelFallecido = findViewById(R.id.labelFallecido);

        btnEditarInfoAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambioVisibilidad(true);
                btnEditarInfoAutor.setVisibility(View.GONE);
                btnActualizar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambioVisibilidad(false);
                btnEditarInfoAutor.setVisibility(View.VISIBLE);
                btnActualizar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
            }
        });

        rb_si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_si.isChecked()) {
                    labelFallecido.setVisibility(View.VISIBLE);
                    fechaFallecimiento.setVisibility(View.VISIBLE);
                }
            }
        });

        rb_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_no.isChecked()) {
                    labelFallecido.setVisibility(View.GONE);
                    fechaFallecimiento.setVisibility(View.GONE);
                }
            }
        });

        Intent intent = getIntent();
        String nombreAutor = intent.getStringExtra("nombre");
        idAutorClicado = intent.getIntExtra("idAutor",0);


        //Traigo todos los datos del autor clicado y los pongo en las cajas de texto indicadas
        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Autores");

                while(rs.next()){
                    int idAutorBBDD = rs.getInt(1);
                    if(idAutorBBDD == idAutorClicado){
                        autorBBDD = new Autor(rs.getInt(1) , rs.getString(2) , rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
                        nombre.setText(autorBBDD.getNombre());
                        apellidos.setText(autorBBDD.getApellidos());
                        nacionalidad.setText(autorBBDD.getNacionalidad());
                        fechaNacimiento.setText(autorBBDD.getFechaNacimiento());
                        if(rs.getString(6)!=null){
                            labelFallecido.setVisibility(View.VISIBLE);
                            rb_si.setChecked(true);
                            fechaFallecimiento.setText(autorBBDD.getFechaFallecimiento());
                        }else{
                            rb_no.setChecked(true);
                            labelFallecido.setVisibility(View.GONE);
                            fechaFallecimiento.setVisibility(View.GONE);
                        }

                    }
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: a"+exception);
        }


        btnEliminarAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(conn!=null){

                        PreparedStatement stm = conexionBD().prepareStatement("DELETE FROM Libros WHERE AutorID="+idAutorClicado);
                        stm.executeUpdate();

                        PreparedStatement stm2 = conexionBD().prepareStatement("DELETE FROM Autores WHERE AutorID="+idAutorClicado);
                        stm2.executeUpdate();

                        Toast.makeText(Informacion_autores.this, "AUTOR ELIMINADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                startActivity(intent2);
                                finish();
                            }
                        },2000);

                    }else{
                        Toast.makeText(Informacion_autores.this,"Error en la eliminación", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"ERROR EN LA ELIMINACIÓN DEL AUTOR",Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }
            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String nombreActualizar = null;
               String apellidosActualizar = null;
               String nacionalidadActualizar = null;
               String fechaNacimientoActualizar = null;
               String fechaFallecimientoActualizar= null;

                //Validación del nombre
                if(!(nombre.getText().toString().isEmpty())){

                    if(autorBBDD.getNombre().equals(nombre.getText().toString())){
                        nombreActualizar =autorBBDD.getNombre();
                    }else{
                        nombreActualizar =(String)nombre.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_autores.this, "El nombre no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de los apellidos
                if(!(apellidos.getText().toString().isEmpty())){

                    if(autorBBDD.getApellidos().equals(apellidos.getText().toString())){
                        apellidosActualizar =autorBBDD.getApellidos();
                    }else{
                        apellidosActualizar =(String)apellidos.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_autores.this, "Los apellidos no pueden quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la nacionalidad
                if(!(nacionalidad.getText().toString().isEmpty())){

                    if(autorBBDD.getNacionalidad().equals(nacionalidad.getText().toString())){
                        nacionalidadActualizar =autorBBDD.getNacionalidad();
                    }else{
                        nacionalidadActualizar =(String)nacionalidad.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_autores.this, "La nacionalidad no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación de la fecha de nacimiento
                if(!(fechaNacimiento.getText().toString().isEmpty())){
                    if(autorBBDD.getFechaNacimiento().equals(fechaNacimiento.getText().toString())){
                        fechaNacimientoActualizar =autorBBDD.getFechaNacimiento();
                    }else{
                        String formatoFecha = "yyyy/MM/dd";
                        String fechafecha =(String) fechaNacimiento.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                        sdf.setLenient(false);

                        try {
                            Date fecha = sdf.parse(fechafecha);
                            fechaNacimientoActualizar = fechafecha;
                        }catch (ParseException e) {
                            Toast.makeText(Informacion_autores.this, "La fecha de nacimiento ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else{
                    Toast.makeText(Informacion_autores.this, "La fecha de nacimiento no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }






                if(nombreActualizar!=null && apellidosActualizar!=null && nacionalidadActualizar!=null && fechaNacimientoActualizar!=null){

                    if(rb_si.isChecked()){
                        //Validación de la fecha de fallecimiento
                        if(!(fechaFallecimiento.getText().toString().isEmpty())){
                                String formatoFecha = "yyyy/MM/dd";
                                String fechafecha =(String) fechaFallecimiento.getText().toString();

                                SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                                sdf.setLenient(false);

                                try {
                                    Date fecha = sdf.parse(fechafecha);
                                    fechaFallecimientoActualizar = fechaFallecimiento.getText().toString();
                                    Log.d(TAG, "onClick: fecha____"+fechaFallecimientoActualizar);

                                }catch (ParseException e) {
                                    Toast.makeText(Informacion_autores.this, "La fecha de fallecimiento ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                                }


                            if(fechaFallecimientoActualizar!=null){
                                Toast.makeText(Informacion_autores.this, fechaFallecimientoActualizar+"__GOOD", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(Informacion_autores.this, "La fecha de fallecimiento no puede quedar vacia", Toast.LENGTH_SHORT).show();
                        }
                    } else if (rb_no.isChecked()) {
                        Toast.makeText(Informacion_autores.this, fechaFallecimientoActualizar+"__BAD", Toast.LENGTH_SHORT).show();
                    }


                    /*
                    try {
                        if(conn!=null){

                            PreparedStatement stm = conexionBD().prepareStatement("UPDATE Autores SET Nombre='"+ nombreActualizar +"',Apellidos='"+ apellidosActualizar +"',Nacionalidad='"+ nacionalidadActualizar +"',FechaNacimiento='"+ fechaNacimientoActualizar +"',FechaFallecimiento='"+ fechaFallecimientoActualizar +"' WHERE AutorID='"+ idAutorClicado +"'");
                            stm.executeUpdate();

                            Toast.makeText(Informacion_autores.this, "DATOS ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                            startActivity(intent2);

                        }

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    }

                     */
                }else{
                    Toast.makeText(getApplicationContext(),"NO ESTÁ TODO BIEN",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    private void cambioVisibilidad(Boolean a){
        nombre.setClickable(a);
        nombre.setFocusable(a);
        nombre.setEnabled(a);
        nombre.setFocusableInTouchMode(a);

        apellidos.setClickable(a);
        apellidos.setFocusable(a);
        apellidos.setEnabled(a);
        apellidos.setFocusableInTouchMode(a);

        nacionalidad.setClickable(a);
        nacionalidad.setFocusable(a);
        nacionalidad.setEnabled(a);
        nacionalidad.setFocusableInTouchMode(a);

        fechaNacimiento.setClickable(a);
        fechaNacimiento.setFocusable(a);
        fechaNacimiento.setActivated(a);
        fechaNacimiento.setEnabled(a);
        fechaNacimiento.setFocusableInTouchMode(a);

        rb_si.setClickable(a);

        rb_si.setEnabled(a);
        rb_si.setFocusableInTouchMode(a);

        rb_no.setClickable(a);

        rb_no.setEnabled(a);
        rb_no.setFocusableInTouchMode(a);

        fechaFallecimiento.setClickable(a);
        fechaFallecimiento.setFocusable(a);
        fechaFallecimiento.setActivated(a);
        fechaFallecimiento.setEnabled(a);
        fechaFallecimiento.setFocusableInTouchMode(a);

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