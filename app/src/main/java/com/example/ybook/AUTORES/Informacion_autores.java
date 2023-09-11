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
import com.example.ybook.Entidades.Libro;
import com.example.ybook.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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