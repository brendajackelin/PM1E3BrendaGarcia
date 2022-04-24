package com.example.pm1e3brendagarcia.modelo;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Blob;

public class Medicamento implements Serializable {

    private int id;
    private String descripcion;
    private int cantidad;
    private String horas;
    private int periocidad;
    private byte[] imagen;


    public Medicamento() {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.horas = horas;
        this.periocidad = periocidad;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public byte[] getImagen() {  return imagen;  }

    public void setImagen(byte[] imagen) {  this.imagen = imagen; }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) { this.horas = horas;  }

    public int getPeriocidad() {
        return periocidad;
    }

    public void setPeriocidad(int periocidad) {
        this.periocidad = periocidad;
    }
}
