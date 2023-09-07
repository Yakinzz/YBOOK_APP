package com.example.ybook;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.Entidades.Autor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class Informacion_libro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Informacion_libro";
    Spinner comboAutor;
    ArrayList<String> listaAutores = new ArrayList<String>();
    ArrayList<Autor> autoresList = new ArrayList<Autor>();

    int idAutorClicado = 0;

    Button btnEditarInfoLibro;
    Button btnAceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_libro);

        Log.d(TAG, "onCreate: called");
        TextView titulo =findViewById(R.id.txtTitulo);
        TextView editorial =findViewById(R.id.txtEditorial);
        TextView idioma =findViewById(R.id.txtIdioma);
        TextView categoria =findViewById(R.id.txtCategoria);
        TextView fechaPublicacion = findViewById(R.id.txtFechaPublicacion);
        TextView numeroPaginas = findViewById(R.id.txtNumeroPaginas);
        comboAutor = (Spinner) findViewById(R.id.sp_autor);
        btnEditarInfoLibro = findViewById(R.id.btnEditarInformacionLibro);
        btnAceptar = findViewById(R.id.btnAceptarCambios);


        btnEditarInfoLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titulo.setClickable(true);
                titulo.setFocusable(true);
                titulo.setActivated(true);
                titulo.setEnabled(true);
                //titulo.setFocusableInTouchMode(true);

                editorial.setClickable(true);
                editorial.setFocusable(true);
                editorial.setActivated(true);
                editorial.setEnabled(true);

                idioma.setClickable(true);
                idioma.setFocusable(true);
                idioma.setActivated(true);
                idioma.setEnabled(true);

                categoria.setClickable(true);
                categoria.setFocusable(true);
                categoria.setActivated(true);
                categoria.setEnabled(true);

                fechaPublicacion.setClickable(true);
                fechaPublicacion.setFocusable(true);
                fechaPublicacion.setActivated(true);
                fechaPublicacion.setEnabled(true);

                numeroPaginas.setClickable(true);
                numeroPaginas.setFocusable(true);
                numeroPaginas.setActivated(true);
                numeroPaginas.setEnabled(true);

                comboAutor.setClickable(true);
                comboAutor.setFocusable(true);
                comboAutor.setActivated(true);
                comboAutor.setEnabled(true);


                btnEditarInfoLibro.setVisibility(View.GONE);
                btnAceptar.setVisibility(View.VISIBLE);


            }
        });


        Intent intent = getIntent();
        String titulo1 = intent.getStringExtra("titulo");
        idAutorClicado = intent.getIntExtra("idAutor",0);

        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Libros");

                while(rs.next()){
                    String tituloBBDD = rs.getString(2);
                    int id_autor= 0;
                    if(titulo1.equals(tituloBBDD)){
                        titulo.setText(tituloBBDD);
                        editorial.setText(rs.getString(5));
                        idioma.setText(rs.getString(4));
                        categoria.setText(rs.getString(6));
                        fechaPublicacion.setText(rs.getString(7));
                        numeroPaginas.setText(rs.getString(8));
                        id_autor= rs.getInt(3);
                    }
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }


        comboAutor.setOnItemSelectedListener(this);
        seleccionarAutor(idAutorClicado);

        consultarAutores();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaAutores);

        comboAutor.setAdapter(adaptador);

    }

    private void consultarAutores(){
        Autor autor;
        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Autores");

                while(rs.next()){
                    autor = new Autor();
                    autor.setId(rs.getInt(1));
                    autor.setNombre(rs.getString(2));
                    autor.setApellidos(rs.getString(3));

                    autoresList.add(autor);
                }
                obtenerLista();
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }
    }

    private void seleccionarAutor(int id){

        for(int i=0;i<autoresList.size();i++){
            if(autoresList.get(i).getId() == id){
                for(int j=0;i<listaAutores.size();j++){
                    int idenlista =Integer.parseInt(listaAutores.get(j).split(" - ")[0]);
                    if(idenlista == id){
                        comboAutor.setSelection(j);
                    }
                }
            }
        }
    }

    private void  obtenerLista(){
        for(int i=0;i<autoresList.size();i++){
            listaAutores.add(autoresList.get(i).getId() + " - " + autoresList.get(i).getNombre() + " " + autoresList.get(i).getApellidos());
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String item = parent.getItemAtPosition(pos).toString();
        int idd_autor =Integer.parseInt(item.split(" - ")[0]);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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