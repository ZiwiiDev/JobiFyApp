package com.example.jobifyapp.modelo;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;

import java.io.Serializable;
// -----------------------------------------------------------------------------------------------------------------------------
public class Candidato extends Usuario implements Serializable {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private String experiencia;
    private String educacion;
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR VACÍO */
    public Candidato(){}
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Candidato(int id, String nombre, String email, String username, String password, String telefono, String direccion, String experiencia, String educacion) {
        super(id, nombre, email, username, password, telefono, direccion);

        this.experiencia = experiencia;
        this.educacion = educacion;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* GETTERS Y SETTERS */
    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getEducacion() {
        return educacion;
    }

    public void setEducacion(String educacion) {
        this.educacion = educacion;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* toString() */
    @NonNull
    @Override
    public String toString() {
        return "Candidato{" + "experiencia='" + experiencia + ", educacion='" + educacion + '}';
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
