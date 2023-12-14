package com.example.jobifyapp.modelo;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
// -----------------------------------------------------------------------------------------------------------------------------
public class Solicitud implements Serializable {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private int id;
    private Date fecha;
    private byte[] archivo;
    // -----------------------------------------------------------------------------------------------------------------------------
    private Vacante idVacante;
    private Usuario idUsuario;
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR VACÍO */
    public Solicitud(){}
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Solicitud(int id, Date fecha, byte[] archivo, Vacante idVacante, Usuario idUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.archivo = archivo;
        this.idVacante = idVacante;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public Vacante getIdVacante() {
        return idVacante;
    }

    public void setIdVacante(Vacante idVacante) {
        this.idVacante = idVacante;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* toString() */
    @NonNull
    @Override
    public String toString() {
        return "Solicitud{" + "id=" + id + ", fecha=" + fecha + ", archivo=" + Arrays.toString(archivo) + ", idVacante=" + idVacante +
                ", idUsuario=" + idUsuario + '}';
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
