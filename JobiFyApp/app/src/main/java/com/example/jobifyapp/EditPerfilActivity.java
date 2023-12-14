package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.utils.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// -----------------------------------------------------------------------------------------------------------------------------
public class EditPerfilActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private Intent volverPerfil;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView emailPerfil, experienciaSectorDependeTitulo, educacionWebDependeTitulo;
    private EditText nombrePerfil, usuarioPerfil, telefonoPerfilUsuario, direccionPerfilUsuario, experienciaSectorDepende, educacionWebDepende;
    private Button btnVolver, btnActualizarPerfil;

    private ControladorBD baseDatosControlador;
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        baseDatosControlador = new ControladorBD(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        btnVolver = findViewById(R.id.btnVolverPerfil);
        emailPerfil = findViewById(R.id.emailPerfil);
        nombrePerfil = findViewById(R.id.nombrePerfil);
        usuarioPerfil = findViewById(R.id.UsuarioPerfil);
        telefonoPerfilUsuario = findViewById(R.id.TelefonoPerfilUsuario);
        direccionPerfilUsuario = findViewById(R.id.DireccionPerfilUsuario);
        experienciaSectorDepende = findViewById(R.id.experienciaSectorDepende);
        educacionWebDepende = findViewById(R.id.educacionWebDepende);
        experienciaSectorDependeTitulo = findViewById(R.id.experienciaSectorDependeTitulo);
        educacionWebDependeTitulo = findViewById(R.id.educacionWebDependeTitulo);
        btnActualizarPerfil = findViewById(R.id.btnActualizarPerfil);

        volverPerfil = new Intent(EditPerfilActivity.this, PerfilActivity.class);

        if (user != null && user.getEmail() != null) {
            String userId = user.getUid(); // Identificador único del usuario actual

            // Nombre de preferencias compartidas único para ese usuario
            String prefsName = "MisPrefsUsuario_" + userId;

            String email = user.getEmail();

            String tipoUsuario = UserUtil.getTipoUsuario();

            int usuarioId = baseDatosControlador.obtenerIdUsuario(email);
            Usuario usuario = baseDatosControlador.obtenerDatosUsuario(usuarioId);

            emailPerfil.setText(email);

            if (tipoUsuario != null && tipoUsuario.equals("candidato")) {
                // Al actualizar experiencia y educacion
                SharedPreferences prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                experienciaSectorDependeTitulo.setText(getString(R.string.setTextExperiencia));
                educacionWebDependeTitulo.setText(getString(R.string.setTextEducacion));

                experienciaSectorDepende.setHint(getString(R.string.setHintExperiencia));
                educacionWebDepende.setHint(getString(R.string.setHintEducacion));

                btnActualizarPerfil.setOnClickListener(v -> {
                    // Obtener los valores de los campos de texto
                    String nombre = nombrePerfil.getText().toString();
                    String username = usuarioPerfil.getText().toString().replaceAll("\\s+", "");
                    String telefono = telefonoPerfilUsuario.getText().toString().trim();
                    String direccion = direccionPerfilUsuario.getText().toString();
                    String experiencia = experienciaSectorDepende.getText().toString();
                    String educacion = educacionWebDepende.getText().toString();

                    boolean actualizado = false; // Variable para verificar si se ha realizado alguna actualización

                    if (!nombre.isEmpty()) {
                        usuario.setNombre(nombre);
                        baseDatosControlador.actualizarNombre(usuarioId, nombre);
                        actualizado = true;
                    }

                    if (!username.isEmpty()) {
                        usuario.setUsername(username);
                        baseDatosControlador.actualizarUsername(usuarioId, username);
                        actualizado = true;
                    }

                    if (!telefono.isEmpty()) {
                        if (telefono.length() != 9) {
                            Toast.makeText(EditPerfilActivity.this, R.string.telefonoCaracteres, Toast.LENGTH_SHORT).show();
                            return; // Salir del método onClick sin continuar con las actualizaciones
                        }

                        usuario.setTelefono(telefono);
                        baseDatosControlador.actualizarTelefono(usuarioId, telefono);
                        actualizado = true;
                    }

                    if (!direccion.isEmpty()) {
                        usuario.setDireccion(direccion);
                        baseDatosControlador.actualizarDireccion(usuarioId, direccion);
                        actualizado = true;
                    }

                    if (!experiencia.isEmpty()) {
                        Log.e("experiencia", experiencia);
                        baseDatosControlador.actualizarExperiencia(usuarioId, experiencia);
                        actualizado = true;
                        editor.putString("experiencia", experiencia);
                    }

                    if (!educacion.isEmpty()) {
                        Log.e("educacion", educacion);
                        baseDatosControlador.actualizarEducacion(usuarioId, educacion);
                        actualizado = true;
                        editor.putString("educacion", educacion);
                    }

                    if (actualizado) {
                        editor.apply();

                        // Mensaje de éxito de actualización de datos del usuario candidato
                        Toast.makeText(EditPerfilActivity.this, getString(R.string.successCandidatoUpdate), Toast.LENGTH_SHORT).show();
                        startActivity(volverPerfil);
                        finish();
                    }
                });
            } else if (tipoUsuario != null && tipoUsuario.equals("empresa")) {
                // Al actualizar experiencia y educacion
                SharedPreferences prefs = getSharedPreferences(prefsName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                experienciaSectorDependeTitulo.setText(getString(R.string.setTextSector));
                educacionWebDependeTitulo.setText(getString(R.string.setTextWeb));

                experienciaSectorDepende.setHint(getString(R.string.setHintSector));
                educacionWebDepende.setHint(getString(R.string.setHintWeb));

                btnActualizarPerfil.setOnClickListener(v -> {
                    // Obtener los valores de los campos de texto
                    String nombre = nombrePerfil.getText().toString();
                    String username = usuarioPerfil.getText().toString().replaceAll("\\s+", "");
                    String telefono = telefonoPerfilUsuario.getText().toString().trim();
                    String direccion = direccionPerfilUsuario.getText().toString();
                    String sector = experienciaSectorDepende.getText().toString().trim();
                    String web = educacionWebDepende.getText().toString().trim();

                    boolean actualizado = false; // Variable para verificar si se ha realizado alguna actualización

                    if (!nombre.isEmpty()) {
                        usuario.setNombre(nombre);
                        baseDatosControlador.actualizarNombre(usuarioId, nombre);
                        actualizado = true;
                    }

                    if (!username.isEmpty()) {
                        usuario.setUsername(username);
                        baseDatosControlador.actualizarUsername(usuarioId, username);
                        actualizado = true;
                    }

                    if (!telefono.isEmpty()) {
                        if (telefono.length() != 9) {
                            Toast.makeText(EditPerfilActivity.this, R.string.telefonoCaracteres, Toast.LENGTH_SHORT).show();
                            return; // Salir del método onClick sin continuar con las actualizaciones
                        }

                        usuario.setTelefono(telefono);
                        baseDatosControlador.actualizarTelefono(usuarioId, telefono);
                        actualizado = true;
                    }

                    if (!direccion.isEmpty()) {
                        usuario.setDireccion(direccion);
                        baseDatosControlador.actualizarDireccion(usuarioId, direccion);
                        actualizado = true;
                    }

                    if (!sector.isEmpty()) {
                        baseDatosControlador.actualizarSector(usuarioId, sector);
                        actualizado = true;
                        editor.putString("sector", sector);
                    }

                    if (!web.isEmpty()) {
                        baseDatosControlador.actualizarWeb(usuarioId, web);
                        actualizado = true;
                        editor.putString("web", web);
                    }

                    if (actualizado) {
                        editor.apply();

                        // Mensaje de éxito de actualización de datos del usuario empresa
                        Toast.makeText(EditPerfilActivity.this, getString(R.string.successEmpresaUpdate), Toast.LENGTH_SHORT).show();
                        startActivity(volverPerfil);
                        finish();
                    }
                });
            } else {
                // Manejar caso de error o usuario desconocido
                Toast.makeText(EditPerfilActivity.this, R.string.usuarioNoReconocido, Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);        // El usuario no está autenticado, redirigir a la pantalla de inicio de sesión
            startActivity(intent);
            finish();
        }

        btnVolver.setOnClickListener(view -> {
            Intent intent = new Intent(EditPerfilActivity.this, PerfilActivity.class);
            startActivity(intent);
            finish();
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
