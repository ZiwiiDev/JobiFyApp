package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.utils.UserUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
// -----------------------------------------------------------------------------------------------------------------------------
public class PerfilActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Toolbar toolbarPerfil;

    private BottomNavigationView bottomNavigationViewEmpresa, bottomNavigationViewCandidato, bottom_navigation_perfil_admin;

    private TextView emailPerfilActualizado, nombrePerfilActualizado, usuarioPerfilUsuarioActualizado,
            TelefonoPerfilUsuario, DireccionPerfilUsuarioActualizado,
            experienciaSectorDependeActualizado, educacionWebDependeActualizado,
            experienciaSectorDependeTituloActualizado, educacionWebDependeTituloActualizado;

    private ImageView perfilUsuarioActualizado;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ControladorBD baseDatosControlador;
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        /* Referencia al toolbar */
        toolbarPerfil = findViewById(R.id.toolbarPerfil);
        setSupportActionBar(toolbarPerfil);

        TelefonoPerfilUsuario = findViewById(R.id.TelefonoPerfilUsuario);
        DireccionPerfilUsuarioActualizado = findViewById(R.id.DireccionPerfilUsuarioActualizado);
        usuarioPerfilUsuarioActualizado = findViewById(R.id.usuarioPerfilUsuarioActualizado);
        nombrePerfilActualizado = findViewById(R.id.nombrePerfilActualizado);
        perfilUsuarioActualizado = findViewById(R.id.perfilUsuarioActualizado);
        emailPerfilActualizado = findViewById(R.id.emailPerfilActualizado);
        experienciaSectorDependeActualizado = findViewById(R.id.experienciaSectorDependeActualizado);
        educacionWebDependeActualizado = findViewById(R.id.educacionWebDependeActualizado);

        bottomNavigationViewCandidato = findViewById(R.id.bottom_navigation_perfil_candidato);
        bottom_navigation_perfil_admin = findViewById(R.id.bottom_navigation_perfil_admin);
        experienciaSectorDependeTituloActualizado = findViewById(R.id.experienciaSectorDependeTituloActualizado);
        educacionWebDependeTituloActualizado = findViewById(R.id.educacionWebDependeTituloActualizado);
        bottomNavigationViewEmpresa = findViewById(R.id.bottom_navigation_perfil);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null && currentUser.getEmail() != null) {
            String userId = currentUser.getUid(); // Identificador único del usuario actual

            // Nombre de preferencias compartidas único para ese usuario
            String prefsName = "MisPrefsUsuario_" + userId;

            String email = currentUser.getEmail();
            String tipoUsuario = UserUtil.getTipoUsuario();

            Log.e("TipoUsuario", tipoUsuario);

            int usuarioId = baseDatosControlador.obtenerIdUsuario(email);
            Usuario usuario = baseDatosControlador.obtenerDatosUsuario(usuarioId);

            if (usuario != null) {
                // Asignar los valores a los campos de texto
                nombrePerfilActualizado.setText(usuario.getNombre());
                emailPerfilActualizado.setText(email);
                usuarioPerfilUsuarioActualizado.setText(usuario.getUsername().toLowerCase(Locale.ROOT));
                TelefonoPerfilUsuario.setText(usuario.getTelefono());
                DireccionPerfilUsuarioActualizado.setText(usuario.getDireccion());

                if (tipoUsuario != null && tipoUsuario.equals("candidato")) {
                    bottomNavigationViewCandidato.setVisibility(View.VISIBLE);
                    bottomNavigationViewEmpresa.setVisibility(View.GONE);
                    bottom_navigation_perfil_admin.setVisibility(View.GONE);

                    experienciaSectorDependeTituloActualizado.setText(getString(R.string.setTextExperiencia));
                    educacionWebDependeTituloActualizado.setText(getString(R.string.setTextEducacion));

                    SharedPreferences prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
                    String experiencia = prefs.getString("experiencia", "");
                    String educacion = prefs.getString("educacion", "");

                    experienciaSectorDependeActualizado.setText(experiencia);
                    educacionWebDependeActualizado.setText(educacion);

                    perfilUsuarioActualizado.setImageResource(R.mipmap.iconocandidato_foreground);

                    bottomNavigationViewCandidato.setOnNavigationItemSelectedListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.inicio:
                                // Acción al hacer clic en el elemento de inicio
                                Intent inicio = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(inicio);
                                return true;
                            case R.id.vacantes:
                                // Acción al hacer clic en el elemento de vacantes
                                Intent perfil = new Intent(getApplicationContext(), VacantesActivity.class);
                                startActivity(perfil);
                                return true;
                        }
                        return false;
                    });
                } else if (tipoUsuario != null && tipoUsuario.equals("empresa")) {
                    bottomNavigationViewCandidato.setVisibility(View.GONE);
                    bottomNavigationViewEmpresa.setVisibility(View.VISIBLE);
                    bottom_navigation_perfil_admin.setVisibility(View.GONE);

                    experienciaSectorDependeTituloActualizado.setText(getString(R.string.setTextSector));
                    educacionWebDependeTituloActualizado.setText(getString(R.string.setTextWeb));

                    SharedPreferences prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
                    String sector = prefs.getString("sector", "");
                    String web = prefs.getString("web", "");

                    experienciaSectorDependeActualizado.setText(sector);
                    educacionWebDependeActualizado.setText(web);

                    perfilUsuarioActualizado.setImageResource(R.mipmap.iconoempresa_foreground);

                    // Escuchar los eventos de selección de elemento en la navegación inferior
                    bottomNavigationViewEmpresa.setOnNavigationItemSelectedListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.inicio:
                                // Acción al hacer clic en el elemento de inicio
                                Intent vacantes = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(vacantes);
                                return true;
                            case R.id.vacantes:
                                // Acción al hacer clic en el elemento de perfil
                                Intent perfil = new Intent(getApplicationContext(), VacantesActivity.class);
                                startActivity(perfil);
                                return true;
                            case R.id.solicitudes:
                                // Acción al hacer clic en el elemento de perfil
                                Intent solicitudes = new Intent(getApplicationContext(), SolicitudesActivity.class);
                                startActivity(solicitudes);
                                return true;
                        }
                        return false;
                    });
                } else if (tipoUsuario != null && tipoUsuario.equals("admin")) {
                    bottomNavigationViewCandidato.setVisibility(View.GONE);
                    bottomNavigationViewEmpresa.setVisibility(View.GONE);
                    bottom_navigation_perfil_admin.setVisibility(View.VISIBLE);

                    experienciaSectorDependeTituloActualizado.setText("");
                    educacionWebDependeTituloActualizado.setText("");

                    TelefonoPerfilUsuario.setText(usuario.getTelefono());
                    DireccionPerfilUsuarioActualizado.setText(usuario.getDireccion());

                    perfilUsuarioActualizado.setImageResource(R.mipmap.iconoadmin_foreground);

                    // Escuchar los eventos de selección de elemento en la navegación inferior
                    bottom_navigation_perfil_admin.setOnNavigationItemSelectedListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.vacantes:
                                // Acción al hacer clic en el elemento de inicio
                                Intent vacantes = new Intent(getApplicationContext(), VacantesActivity.class);
                                startActivity(vacantes);
                                return true;
                            case R.id.categorias:
                                // Acción al hacer clic en el elemento de categorias
                                Intent solicitudes = new Intent(getApplicationContext(), CategoriasActivity.class);
                                startActivity(solicitudes);
                                return true;
                            case R.id.usuarios:
                                // Acción al hacer clic en el elemento de usuarios
                                Intent usuarios = new Intent(getApplicationContext(), UsuariosActivity.class);
                                startActivity(usuarios);
                                return true;
                        }
                        return false;
                    });
                } else {
                    Toast.makeText(PerfilActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
                }
            } else {
                // Usuario no encontrado en la base de datos
                nombrePerfilActualizado.setText("");
                emailPerfilActualizado.setText("");
                usuarioPerfilUsuarioActualizado.setText("");
                TelefonoPerfilUsuario.setText("");
                DireccionPerfilUsuarioActualizado.setText("");
                experienciaSectorDependeActualizado.setText("");
                educacionWebDependeActualizado.setText("");
            }
        } else {
            // Usuario no autenticado
            nombrePerfilActualizado.setText("");
            emailPerfilActualizado.setText("");
            usuarioPerfilUsuarioActualizado.setText("");
            TelefonoPerfilUsuario.setText("");
            DireccionPerfilUsuarioActualizado.setText("");
            experienciaSectorDependeActualizado.setText("");
            educacionWebDependeActualizado.setText("");

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);       // El usuario no está autenticado, redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflar el menú correspondiente según el tipo de usuario
        String tipoUsuario = UserUtil.getTipoUsuario();

        // Verificar el tipo de usuario y gestionar las selecciones de opciones de menú correspondientes
        if (tipoUsuario != null && (tipoUsuario.equals("candidato") || tipoUsuario.equals("empresa") )) {
            getMenuInflater().inflate(R.menu.menu_perfil_controlador, menu);
        } else if (tipoUsuario != null && tipoUsuario.equals("admin")) {
            getMenuInflater().inflate(R.menu.menu_perfil_controlador_admin, menu);
        } else {
            Toast.makeText(PerfilActivity.this, getString(R.string.errorUsuario), Toast.LENGTH_SHORT).show();     // El tipo de usuario es desconocido o no está definido
        }
        return true;
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint({"NonConstantResourceId", "InflateParams"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Verificar el tipo de usuario actual
        String tipoUsuario = UserUtil.getTipoUsuario();

        // Verificar el tipo de usuario y gestionar las selecciones de opciones de menú correspondientes
        if (tipoUsuario != null && (tipoUsuario.equals("candidato") || tipoUsuario.equals("empresa"))) {
            switch (item.getItemId()) {
                case R.id.editarPerfil:
                    Intent i = new Intent(getApplicationContext(), EditPerfilActivity.class);
                    startActivity(i);
                    break;
                case R.id.eliminarPerfil:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setView(getLayoutInflater().inflate(R.layout.dialog_confirm_borrar_cuenta, null));
                    builder.setCancelable(false);
                    builder.setPositiveButton("No", (dialog, id) -> {
                        // Si le da a que "No" no hace nada
                    });

                    builder.setNegativeButton("Sí", (dialog, id) -> {
                        // Si le da a que "Sí" se elimina la cuenta
                        eliminarCuenta();
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                case R.id.btnLogoutPerfil:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setView(getLayoutInflater().inflate(R.layout.dialog_confirm, null));
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("No", (dialog, id) -> {
                        // Si le da a que "No" no hace nada
                    });

                    builder1.setNegativeButton("Sí", (dialog, id) -> {
                        // Si le da a que "Sí" cierra sesión
                        FirebaseAuth.getInstance().signOut(); // Cierro la sesión y vuelvo a la pantalla de login
                        UserUtil.setTipoUsuario(null);
                        Intent e = new Intent(getApplicationContext(), InicioActivity.class);
                        startActivity(e);
                        finish();
                    });

                    AlertDialog alert2 = builder1.create();
                    alert2.show();
                    break;
            }
        } else if (tipoUsuario != null && tipoUsuario.equals("admin")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(getLayoutInflater().inflate(R.layout.dialog_confirm, null));
                builder1.setCancelable(false);
                builder1.setPositiveButton("No", (dialog, id) -> {
                    // Si le da a que "No" no hace nada
                });

                builder1.setNegativeButton("Sí", (dialog, id) -> {
                    // Si le da a que "Sí" cierra sesión
                    FirebaseAuth.getInstance().signOut(); // Cierro la sesión y vuelvo a la pantalla de login
                    UserUtil.setTipoUsuario(null);
                    Intent e = new Intent(getApplicationContext(), InicioActivity.class);
                    startActivity(e);
                    finish();
                });

                AlertDialog alert2 = builder1.create();
                alert2.show();
        }
        return super.onOptionsItemSelected(item);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void eliminarCuenta() {
        if (currentUser != null) {
            currentUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.correctoEliminarCuenta), Toast.LENGTH_SHORT).show();        // La cuenta se eliminó correctamente

                            Intent intent = new Intent(getApplicationContext(), InicioActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, getString(R.string.errorEliminarCuenta), Toast.LENGTH_SHORT).show();           // Ocurrió un error al eliminar la cuenta
                        }
                    });
        } else {
            // El usuario no está autenticado o la cuenta ya ha sido eliminada previamente
            Toast.makeText(this, getString(R.string.cuentaEliminarError), Toast.LENGTH_SHORT).show();
        }
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
