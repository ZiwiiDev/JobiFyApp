package com.example.jobifyapp.modelo;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
// -----------------------------------------------------------------------------------------------------------------------------
public class Vacante implements Serializable {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private int id;
    private String nombre;
    private Date fecha;
    private double salario;
    private int destacado;
    private String detalles;

    private Categoria idCategoria;
    private Empresa idUsuario;
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR VACÍO */
    public Vacante(){}
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Vacante(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Vacante(int id, String nombre, double salario, int destacado, String detalles, Categoria idCategoria, Empresa idUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = new Date(); // Establecer la fecha actual al crear la instancia
        this.salario = salario;
        this.destacado = destacado;
        this.detalles = detalles;
        this.idCategoria = idCategoria;
        this.idUsuario = idUsuario;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Vacante(String nombre, Date fecha, double salario, int destacado, String detalles, Categoria idCategoria, Empresa idUsuario) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.salario = salario;
        this.destacado = destacado;
        this.detalles = detalles;
        this.idCategoria = idCategoria;
        this.idUsuario = idUsuario;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* GETTERS Y SETTERS */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public int getDestacado() {
        return destacado;
    }

    public void setDestacado(int destacado) {
        this.destacado = destacado;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Empresa getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Empresa idUsuario) {
        this.idUsuario = idUsuario;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* toString() */
    @NonNull
    @Override
    public String toString() {
        return "Vacante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fecha=" + fecha +
                ", salario=" + salario +
                ", destacado=" + destacado +
                ", detalles='" + detalles + '\'' +
                ", idCategoria=" + idCategoria +
                ", idUsuario=" + idUsuario +
                '}';
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
