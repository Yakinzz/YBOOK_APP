package com.example.ybook.models;

public class ListElementAutores {

    private String nombreAutor;
    private int idAutor;

    public ListElementAutores(String nombre,int idAutor) {
        this.nombreAutor = nombre;
        this.idAutor=idAutor;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }


    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

}
