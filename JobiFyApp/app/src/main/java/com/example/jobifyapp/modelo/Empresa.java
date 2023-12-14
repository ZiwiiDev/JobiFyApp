package com.example.jobifyapp.modelo;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;

import java.io.Serializable;
// -----------------------------------------------------------------------------------------------------------------------------
public class Empresa extends Usuario implements Serializable {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private String web;
    private String sector;
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR VACÍO */
    public Empresa(){}
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Empresa(int id, String nombre, String email, String username, String password, String telefono, String direccion, String web, String sector) {
        super(id, nombre, email, username, password, telefono, direccion);

        this.web = web;
        this.sector = sector;
    }
    /* CONSTRUCTOR PARAMETRIZADO */
    public Empresa(int id, String web, String sector) {
        super(id);
        this.web = web;
        this.sector = sector;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* GETTERS Y SETTERS */
    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* toString() */
    @NonNull
    @Override
    public String toString() {
        return "Empresa{" + "web='" + web + ", sector='" + sector + '}';
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
