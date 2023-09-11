package com.example.ybook.LIBROS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.ybook.Entidades.Libro;
import com.example.ybook.PaginaPrincipal;
import com.example.ybook.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Informacion_libro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Informacion_libro";
    Spinner comboAutor;
    ArrayList<String> listaAutores = new ArrayList<String>();
    ArrayList<Autor> autoresList = new ArrayList<Autor>();
    Libro libroBBDD;

    int idAutorClicado = 0;
    int idLibro=0;

    Button btnEditarInfoLibro;
    Button btnEliminarLibro;
    Button btnActualizar;
    Button btnCancelar;
    TextView titulo;
    TextView editorial;
    TextView idioma;
    TextView categoria;
    TextView fechaPublicacion;
    TextView numeroPaginas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_libro);

        Log.d(TAG, "onCreate: called");
        titulo =findViewById(R.id.txtTitulo);
        editorial =findViewById(R.id.txtEditorial);
        idioma =findViewById(R.id.txtIdioma);
        categoria =findViewById(R.id.txtCategoria);
        fechaPublicacion = findViewById(R.id.txtFechaPublicacion);
        numeroPaginas = findViewById(R.id.txtNumeroPaginas);
        comboAutor = (Spinner) findViewById(R.id.sp_autor);
        btnEditarInfoLibro = findViewById(R.id.btnEditarInformacionLibro);
        btnActualizar = findViewById(R.id.btnActualizarCambios);
        btnCancelar = findViewById(R.id.btnCancelarCambios);
        btnEliminarLibro = findViewById(R.id.btnEliminarLibro);


        btnEditarInfoLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambioVisibilidad(true);
                btnEditarInfoLibro.setVisibility(View.GONE);
                btnActualizar.setVisibility(View.VISIBLE);
                btnCancelar.setVisibility(View.VISIBLE);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambioVisibilidad(false);
                btnEditarInfoLibro.setVisibility(View.VISIBLE);
                btnActualizar.setVisibility(View.GONE);
                btnCancelar.setVisibility(View.GONE);
            }
        });



        Intent intent = getIntent();
        String titulo1 = intent.getStringExtra("titulo");
        idLibro = intent.getIntExtra("idLibro",0);
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
                        libroBBDD= new Libro(rs.getInt(1) , rs.getString(2) , rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8));
                        titulo.setText(tituloBBDD);
                        editorial.setText(rs.getString(5));
                        idioma.setText(rs.getString(4));
                        categoria.setText(rs.getString(6));
                        fechaPublicacion.setText(rs.getString(7));
                        numeroPaginas.setText(String.valueOf(rs.getInt(8)));
                        id_autor= rs.getInt(3);
                    }
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA AQUII",Toast.LENGTH_SHORT).show();
        }


        comboAutor.setOnItemSelectedListener(this);
        seleccionarAutor(idAutorClicado);

        consultarAutores();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaAutores);

        comboAutor.setAdapter(adaptador);

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                int idActualizar = libroBBDD.getId();
                String tituloActualizar = null;
                int autorActualizar = idAutorClicado;
                String idiomaActualizar= null;
                String editorialActualizar= null;
                String categoriaActualizar= null;
                String fechaPublicacionActualizar= null;
                int numeroaPaginasActualizar = 0;


                //Validación el título
                if(!(titulo.getText().toString().isEmpty())){

                    if(libroBBDD.getTitulo().equals(titulo.getText().toString())){
                        tituloActualizar =libroBBDD.getTitulo();
                    }else{
                        tituloActualizar =(String)titulo.getText().toString();
                    }

                }else{
                    Toast.makeText(Informacion_libro.this, "El título no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }


                //Validación de la editorial
                if(!(editorial.getText().toString().isEmpty())){
                    if(libroBBDD.getEditorial().equals(editorial.getText().toString())){
                        editorialActualizar =libroBBDD.getEditorial();
                    }else{
                        editorialActualizar = (String) editorial.getText().toString();

                    }
                }else{
                    Toast.makeText(Informacion_libro.this, "La editorial no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del idioma
                if(!(idioma.getText().toString().isEmpty())){
                    if(libroBBDD.getIdioma().equals(idioma.getText().toString())){
                        idiomaActualizar =libroBBDD.getIdioma();
                    }else{
                        idiomaActualizar = (String) idioma.getText().toString();
                    }
                }else{
                    Toast.makeText(Informacion_libro.this, "El idioma no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la categoria
                if(!(categoria.getText().toString().isEmpty())){
                    if(libroBBDD.getCategoria().equals(categoria.getText().toString())){
                        categoriaActualizar =libroBBDD.getCategoria();
                    }else{
                        categoriaActualizar = (String) categoria.getText().toString();
                    }
                }else{
                    Toast.makeText(Informacion_libro.this, "La categoria no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación de la fecha de publicación
                if(!(fechaPublicacion.getText().toString().isEmpty())){
                    if(libroBBDD.getFechaPublicacion().equals(fechaPublicacion.getText().toString())){
                        fechaPublicacionActualizar =libroBBDD.getFechaPublicacion();
                    }else{
                        String formatoFecha = "yyyy/MM/dd";
                        String fechafecha =(String) fechaPublicacion.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                        sdf.setLenient(false);

                        try {
                            Date fecha = sdf.parse(fechafecha);
                            fechaPublicacionActualizar = fechafecha;
                        }catch (ParseException e) {
                            Toast.makeText(Informacion_libro.this, "La fecha ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }else{
                    Toast.makeText(Informacion_libro.this, "La fecha de publicación no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del número de páginas
                if(!(numeroPaginas.getText().toString().isEmpty())){
                    int contenidoCajaNumeroPaginas = Integer.parseInt((String) numeroPaginas.getText().toString());
                    if(libroBBDD.getNumeroPaginas() == contenidoCajaNumeroPaginas){
                        numeroaPaginasActualizar =libroBBDD.getNumeroPaginas();
                    }else{
                        numeroaPaginasActualizar = Integer.parseInt((String) numeroPaginas.getText().toString());
                    }
                }else{
                    Toast.makeText(Informacion_libro.this, "El número de páginas no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }


                //Toast.makeText(getApplicationContext(),tituloActualizar +" "+autorActualizar +" "+ idiomaActualizar+" "+editorialActualizar+" "+ categoriaActualizar +" "+ fechaPublicacionActualizar+" "+ numeroaPaginasActualizar,Toast.LENGTH_SHORT).show();

                if(tituloActualizar!=null && autorActualizar!=0 && idiomaActualizar!=null && editorialActualizar!=null && categoriaActualizar!=null && fechaPublicacionActualizar!=null && numeroaPaginasActualizar!=0){

                    try {
                        if(conn!=null){

                            PreparedStatement stm = conexionBD().prepareStatement("UPDATE Libros SET Titulo='"+ tituloActualizar +"',AutorID='"+ autorActualizar +"',Idioma='"+ idiomaActualizar +"',Editorial='"+ editorialActualizar +"',Categoria='"+ categoriaActualizar +"',FechaPublicacion='"+ fechaPublicacionActualizar +"',NumeroPaginas='"+ numeroaPaginasActualizar +"' WHERE LibroID='"+ idActualizar +"'");
                            stm.executeUpdate();

                            Toast.makeText(Informacion_libro.this, "DATOS ACTUALIZADOS CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getApplicationContext(), libros.class);
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

        btnEliminarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String idLibroo = idLibro+"";
               try {
                    if(conn!=null){
                        PreparedStatement stm3 = conexionBD().prepareStatement("DELETE FROM Valoraciones WHERE LibroID="+idLibroo);
                        stm3.executeUpdate();

                        PreparedStatement stm2 = conexionBD().prepareStatement("DELETE FROM MisLibros WHERE ID_Libro="+idLibroo);
                        stm2.executeUpdate();

                        PreparedStatement stm = conexionBD().prepareStatement("DELETE FROM Libros WHERE LibroID="+idLibroo);
                        stm.executeUpdate();

                        Toast.makeText(Informacion_libro.this, "LIBRO ELIMINADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                startActivity(intent2);
                                finish();
                            }
                        },2000);

                    }else{
                        Toast.makeText(Informacion_libro.this,"Error en la conexion", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception exception){
                    Toast.makeText(getApplicationContext(),"ERROR EN LA ELIMINACIÓN DEL LIBRO",Toast.LENGTH_SHORT).show();
                    exception.printStackTrace();
                }

            }

        });

    }



    private void cambioVisibilidad(Boolean a){
        titulo.setClickable(a);
        titulo.setFocusable(a);
        titulo.setActivated(a);
        titulo.setEnabled(a);
        titulo.setFocusableInTouchMode(a);

        editorial.setClickable(a);
        editorial.setFocusable(a);
        editorial.setActivated(a);
        editorial.setEnabled(a);
        editorial.setFocusableInTouchMode(a);

        idioma.setClickable(a);
        idioma.setFocusable(a);
        idioma.setEnabled(a);
        idioma.setFocusableInTouchMode(a);

        categoria.setClickable(a);
        categoria.setFocusable(a);
        categoria.setActivated(a);
        categoria.setEnabled(a);
        categoria.setFocusableInTouchMode(a);

        fechaPublicacion.setClickable(a);
        fechaPublicacion.setFocusable(a);
        fechaPublicacion.setActivated(a);
        fechaPublicacion.setEnabled(a);
        fechaPublicacion.setFocusableInTouchMode(a);

        numeroPaginas.setClickable(a);
        numeroPaginas.setFocusable(a);
        numeroPaginas.setActivated(a);
        numeroPaginas.setEnabled(a);
        numeroPaginas.setFocusableInTouchMode(a);

        comboAutor.setClickable(a);
        comboAutor.setFocusable(a);
        comboAutor.setActivated(a);
        comboAutor.setEnabled(a);
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