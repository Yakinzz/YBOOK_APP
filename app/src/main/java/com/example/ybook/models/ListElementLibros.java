package com.example.ybook.models;

import java.io.Serializable;

public class ListElementLibros implements Serializable {

    public String titulo;
    public String editorial;
    public String autor;


    public ListElementLibros(String titulo, String eidtorial, String autor) {
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }


}
