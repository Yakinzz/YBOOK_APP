package com.example.ybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ybook.AUTORES.Informacion_autores;
import com.example.ybook.Entidades.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Privacidad extends AppCompatActivity {

    TextView passwordActual;
    TextView passwordNueva;
    TextView passwordNuevaRepetida;

    int idUsuario = 0;
    Usuario usuarioBBDD;
    String passwordBBDD = null;


    Button btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacidad);

        passwordActual=findViewById(R.id.txtPasswordActual);
        passwordNueva = findViewById(R.id.txtPasswordNueva);
        passwordNuevaRepetida=findViewById(R.id.txtPasswordNuevaRepetida);

        btnActualizar=findViewById(R.id.btnActualizarPassword);



        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("ID",0);

        Connection conn = conexionBD();
        try {
            if(conn!=null){
                Statement stm = conexionBD().createStatement();
                ResultSet rs = stm.executeQuery("SELECT * FROM Usuarios WHERE UsuarioID="+idUsuario);

                if(rs.next()){
                    usuarioBBDD=new Usuario(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getString(9),rs.getString(10),rs.getString(11));
                    passwordBBDD=usuarioBBDD.getPassword();
                }
            }

        }catch (Exception exception){
            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
        }

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(passwordActual.getText().toString().isEmpty())){
                    if(passwordActual.getText().toString().equals(passwordBBDD)){
                        if(!(passwordNueva.getText().toString().isEmpty()) && !(passwordNuevaRepetida.getText().toString().isEmpty())){
                            if(passwordNueva.getText().toString().equals(passwordNuevaRepetida.getText().toString())){
                                if(!(passwordNueva.getText().toString().equals(passwordActual.getText().toString()))){
                                    //Aquí valido que la contraseña tenga el formato correcto
                                    String patron = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,16}$";
                                    Pattern pattern = Pattern.compile(patron);
                                    Matcher matcher = pattern.matcher(passwordNueva.getText().toString());

                                    if(matcher.matches()){
                                        try {
                                            if(conn!=null){

                                                PreparedStatement stm = conexionBD().prepareStatement("UPDATE Usuarios SET Password='"+ passwordNueva.getText().toString() +"' WHERE UsuarioID="+ idUsuario +"");
                                                stm.executeUpdate();

                                                Toast.makeText(Privacidad.this, "CONTRASEÑA ACTUALIZADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                                Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                                startActivity(intent2);

                                            }

                                        }catch (Exception exception){
                                            Toast.makeText(getApplicationContext(),"ERROR EN LA CONSULTA",Toast.LENGTH_SHORT).show();
                                            exception.printStackTrace();
                                        }
                                    }else{
                                        Toast.makeText(Privacidad.this, "CONTRASEÑA NO VALIDA", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(Privacidad.this, "Las nueva contraseña es la misma que la actual.", Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(getApplicationContext(), PaginaPrincipal.class);
                                    intent2.putExtra("ID",idUsuario);
                                    startActivity(intent2);

                                }

                            }else{
                                Toast.makeText(Privacidad.this, "Las nuevas contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Privacidad.this, "La nueva contraseña es obligatoria y debe introducirse 2 veces.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Privacidad.this, "La contraseña actual no es correcta.", Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(Privacidad.this, "La contraseña actual es obligatoria.", Toast.LENGTH_SHORT).show();
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