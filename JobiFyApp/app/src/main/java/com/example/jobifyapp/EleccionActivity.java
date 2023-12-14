package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.utils.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
// -----------------------------------------------------------------------------------------------------------------------------
public class EleccionActivity extends AppCompatActivity {
    // -----------------------------------------------------------------------------------------------------------------------------
    /* VARIABLES */
    private FirebaseAuth mAuth;

    private ImageView newUser, alreadyUser, adminInicio;

    private ControladorBD controladorBD;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        controladorBD = new ControladorBD(getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        //Localizo las imagenes que actuarán como botones
        newUser = findViewById(R.id.newuser);
        alreadyUser = findViewById(R.id.alreadyuser);
        adminInicio = findViewById(R.id.adminInicio);

        newUser.setOnClickListener(v -> {
            Intent newUser = new Intent(EleccionActivity.this,NewUserType.class);
            startActivity(newUser);
        });

        alreadyUser.setOnClickListener(v -> {
            Intent alreadyUser = new Intent(EleccionActivity.this,LoginActivity.class);
            startActivity(alreadyUser);
        });

        adminInicio.setOnClickListener(v -> {
            registrarOIniciarSesionAdmin();
        });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void registrarOIniciarSesionAdmin() {
        String email = "admin@jobify.es";
        String contrasenia = "123456";

        mAuth.signInWithEmailAndPassword(email, contrasenia) // Intentar iniciar sesión
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserUtil.setTipoUsuario("admin");
                        Toast.makeText(getApplicationContext(), getString(R.string.exitoAdmin), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EleccionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // El inicio de sesión falló, puede que el administrador no esté registrado
                        registrarAdmin(email, contrasenia);
                    }
                });
        }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void registrarAdmin(String email, String contrasenia) {
        mAuth.createUserWithEmailAndPassword(email, contrasenia)            // Crear un nuevo usuario con el email y contraseña especificados
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrarAdminBD(email, contrasenia);
                        Toast.makeText(getApplicationContext(), R.string.exitoRegistrarAdmin, Toast.LENGTH_SHORT).show();

                        UserUtil.setTipoUsuario("admin");
                        Intent intent = new Intent(EleccionActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // El registro falló
                        Toast.makeText(getApplicationContext(), R.string.errorRegistrar, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    private void registrarAdminBD (String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNombre("Administrador");
        usuario.setEmail(email);
        usuario.setUsername("Admin");
        usuario.setPassword(password);
        usuario.setTelefono("999999999");
        usuario.setDireccion("C/ Alhambra, 1, Almería");

        // Agregar usuario a la base de datos
        controladorBD.agregarUsuario(usuario);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
