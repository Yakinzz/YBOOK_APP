package com.example.ybook;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Informacion_libro extends AppCompatActivity {
    private static final String TAG = "Informacion_libro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_libro);

        Log.d(TAG, "onCreate: called");
        TextView titulo =findViewById(R.id.txtTitulo);
        TextView editorial =findViewById(R.id.txtEditorial);
        TextView idioma =findViewById(R.id.txtIdioma);
        TextView categoria =findViewById(R.id.txtCategoria);
        TextView fechaPublicacion = findViewById(R.id.txtFecha);
        TextView numeroPaginas = findViewById(R.id.txtNumeroPaginas);
        Button calendar = findViewById(R.id.btnCalendar);


        Intent intent = getIntent();
        String titulo1 = intent.getStringExtra("titulo");

        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Libros");

                while(rs.next()){
                    String tituloBBDD = rs.getString(2);
                    if(titulo1.equals(tituloBBDD)){
                        titulo.setText(tituloBBDD);
                        editorial.setText(rs.getString(5));
                        idioma.setText(rs.getString(4));
                        categoria.setText(rs.getString(6));
                        fechaPublicacion.setText(rs.getString(7));
                        numeroPaginas.setText(rs.getString(8));
                    }
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        fechaPublicacion.setText(date);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Informacion_libro.this,calendarFecha.class);
                startActivity(intent);
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