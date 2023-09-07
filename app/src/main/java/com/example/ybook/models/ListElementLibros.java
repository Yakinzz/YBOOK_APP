package com.example.ybook.models;

import java.io.Serializable;

public class ListElementLibros implements Serializable {

    public String titulo;
    public String editorial;
    public int autor;


    public ListElementLibros(String titulo, String eidtorial, int autor) {
        this.titulo = titulo;
        this.editorial = eidtorial;
        this.autor = autor;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAutor() {
        return autor;
    }

    public void setAutor(int autor) {
        this.autor = autor;
    }


}
