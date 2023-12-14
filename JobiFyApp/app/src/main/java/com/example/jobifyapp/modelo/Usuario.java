package com.example.jobifyapp.modelo;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
// -----------------------------------------------------------------------------------------------------------------------------
public class Usuario implements Serializable {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private int id;
    private String nombre;
    private String email;
    private String username;
    private String password;
    private String telefono;
    private String direccion;
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR VACÍO */
    public Usuario(){}
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO */
    public Usuario(int id, String nombre, String email, String username, String password, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO 2 PARA LOGUEAR AL USUARIO */
    public Usuario(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO 3 PARA CARDVIEW */
    public Usuario(String nombre, String email, String username) {
        this.nombre = nombre;
        this.email = email;
        this.username = username;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* CONSTRUCTOR PARAMETRIZADO 4 PARA VACANTES */
    public Usuario(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    /* toString() */
    @NonNull
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
