package com.example.ybook.LIBROS;

import androidx.appcompat.app.AppCompatActivity;

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

public class Add_NuevoLibro extends AppCompatActivity {

    TextView titulo;
    TextView editorial;
    TextView idioma;
    TextView categoria;
    TextView fechaPublicacion;
    TextView numeroPaginas;
    Spinner comboAutor;
    ArrayList<String> listaAutores = new ArrayList<String>();
    ArrayList<Autor> autoresList = new ArrayList<Autor>();
    Libro libroBBDD;

    Button btnCrearNuevoLibro;

    int idAutorSpinner=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nuevo_libro);

        titulo =findViewById(R.id.txtTitulo2);
        editorial =findViewById(R.id.txtEditorial2);
        idioma =findViewById(R.id.txtIdioma2);
        categoria =findViewById(R.id.txtCategoria2);
        fechaPublicacion = findViewById(R.id.txtFechaPublicacion2);
        numeroPaginas = findViewById(R.id.txtNumeroPaginas2);
        btnCrearNuevoLibro = findViewById(R.id.btnAñadirLibro);
        comboAutor = (Spinner) findViewById(R.id.sp_autor);

        consultarAutores();

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaAutores);

        comboAutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el valor seleccionado
                String selectedItem = parent.getItemAtPosition(position).toString();
                idAutorSpinner =Integer.parseInt(selectedItem.split(" - ")[0]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no se selecciona ningún elemento

            }
        });
        comboAutor.setAdapter(adaptador);

        btnCrearNuevoLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tituloNuevo= null;
                String editorialNuevo= null;
                String idiomaNuevo= null;
                String categoriaNuevo= null;
                int autorNuevo = idAutorSpinner;
                String fechaPublicacionNuevo= null;
                int numeroPaginasNuevo = 0;


                //Validación el título
                if(!(titulo.getText().toString().isEmpty())){

                    Connection conn = conexionBD();
                    try {
                        if(conn!=null){
                            Statement stm = conexionBD().createStatement();
                            ResultSet rs = stm.executeQuery("SELECT * FROM Libros");

                            while(rs.next()){
                                String tituloBBDD = rs.getString(2);
                                if(titulo.equals(tituloBBDD)){
                                    Toast.makeText(Add_NuevoLibro.this, "Ya existe un libro con ese título", Toast.LENGTH_SHORT).show();
                                }else{
                                    tituloNuevo =(String)titulo.getText().toString();
                                }
                            }
                        }

                    }catch (Exception exception){
                        Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA AQUII",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(Add_NuevoLibro.this, "El título no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }


                //Validación de la editorial
                if(!(editorial.getText().toString().isEmpty())){
                        editorialNuevo = (String) editorial.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoLibro.this, "La editorial no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del idioma
                if(!(idioma.getText().toString().isEmpty())){
                   idiomaNuevo = (String) idioma.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoLibro.this, "El idioma no puede quedar vacio", Toast.LENGTH_SHORT).show();
                }

                //Validación de la categoria
                if(!(categoria.getText().toString().isEmpty())){
                    categoriaNuevo = (String) categoria.getText().toString();
                }else{
                    Toast.makeText(Add_NuevoLibro.this, "La categoria no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación de la fecha de publicación
                if(!(fechaPublicacion.getText().toString().isEmpty())){
                        String formatoFecha = "yyyy/MM/dd";
                        String fechafecha =(String) fechaPublicacion.getText().toString();

                        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
                        sdf.setLenient(false);

                        try {
                            Date fecha = sdf.parse(fechafecha);
                            fechaPublicacionNuevo = fechafecha;
                        }catch (ParseException e) {
                            Toast.makeText(Add_NuevoLibro.this, "La fecha ingresada no tiene el formato correcto.", Toast.LENGTH_SHORT).show();
                        }


                }else{
                    Toast.makeText(Add_NuevoLibro.this, "La fecha de publicación no puede quedar vacia", Toast.LENGTH_SHORT).show();
                }

                //Validación del número de páginas
                if(!(numeroPaginas.getText().toString().isEmpty())){
                    int contenidoCajaNumeroPaginas = Integer.parseInt((String) numeroPaginas.getText().toString());
                    numeroPaginasNuevo = Integer.parseInt((String) numeroPaginas.getText().toString());

                }else{
                    Toast.makeText(Add_NuevoLibro.this, "El número de páginas no puede quedar vacio", Toast.LENGTH_SHORT).show();

                }

                if(tituloNuevo!=null && editorialNuevo!=null && idiomaNuevo!=null && categoriaNuevo!=null && autorNuevo!=0 && fechaPublicacionNuevo!=null && numeroPaginasNuevo!=0){
                    Connection conn = conexionBD();
                    try {
                        if(conn!=null){

                            PreparedStatement stm = conexionBD().prepareStatement("INSERT INTO Libros (Titulo,AutorID,Idioma,Editorial,Categoria,FechaPublicacion,NumeroPaginas) VALUES ('"+tituloNuevo+"','"+autorNuevo+"','"+idiomaNuevo+"','"+editorialNuevo+"','"+categoriaNuevo+"','"+fechaPublicacionNuevo+"','"+numeroPaginasNuevo+"')");
                            stm.executeUpdate();

                            Toast.makeText(Add_NuevoLibro.this, "LIBRO CREADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
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