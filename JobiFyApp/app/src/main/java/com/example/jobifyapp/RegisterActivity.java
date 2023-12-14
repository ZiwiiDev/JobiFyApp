package com.example.jobifyapp;
// -----------------------------------------------------------------------------------------------------------------------------
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobifyapp.basedatos.ControladorBD;
import com.example.jobifyapp.modelo.Candidato;
import com.example.jobifyapp.modelo.Empresa;
import com.example.jobifyapp.modelo.Usuario;
import com.example.jobifyapp.utils.UserUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// -----------------------------------------------------------------------------------------------------------------------------
public class RegisterActivity extends AppCompatActivity {
    /* VARIABLES */
    private Intent intent;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private EditText edtPassword, edtEmail;
    private ProgressBar progressBar;
    private Button btnRegister, btnVolverRegister;
    private TextView tituloIniciarSesion;
    private ImageView showPassword;

    private ControladorBD controladorBD;

    String tipoUsuario;
    // -----------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserUtil.init(getApplicationContext());             // Para mantener la persistencia de datos

        controladorBD = new ControladorBD(getApplicationContext());

        Log.e("numUsuarios", String.valueOf(controladorBD.numUsuarios()));
        Log.e("numCandidatos", String.valueOf(controladorBD.numCandidato()));
        Log.e("numEmpresa", String.valueOf(controladorBD.numEmpresa()));

        // Instancio FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Obtener el usuario actualmente autenticado
        user = mAuth.getCurrentUser();

        btnRegister = findViewById(R.id.botonRegister);
        btnVolverRegister = findViewById(R.id.btnVolverRegister);
        edtEmail = findViewById(R.id.editTextUsernameRegister);
        edtPassword = findViewById(R.id.editTextPasswordRegister);
        showPassword = findViewById(R.id.iconoContra);
        progressBar = findViewById(R.id.progressBarRegister);
        tituloIniciarSesion = findViewById(R.id.tituloIniciarSesion);

        // Si pulso el botón vuelvo a InicioActivity
        btnVolverRegister.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        tituloIniciarSesion.setOnClickListener(v -> {
            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Configuramos el ImageView como clickable
        showPassword.setClickable(true);
        // Agregamos un listener a nuestro ImageView para cambiar la visibilidad del password
        showPassword.setOnClickListener(v -> {
            boolean passwordVisible = edtPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

            if (passwordVisible) {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showPassword.setImageResource(R.mipmap.iconover_foreground);
                edtPassword.setTextAppearance(getApplicationContext(), R.style.EditTextPassword);
            } else {
                edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showPassword.setImageResource(R.mipmap.icononover_foreground);
                edtPassword.setTextAppearance(getApplicationContext(), R.style.EditTextVisible);
            }

            edtPassword.setSelection(edtPassword.length());
        });

        // ################################   REGISTER    #############################################

        btnRegister.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), R.string.needEmail, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), R.string.needPassword, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Verificar si el email está mal formulado
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getApplicationContext(), R.string.noValidoEmail, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Verificar si la contraseña tiene un mínimo de 6 caracteres
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), R.string.contraCorta, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Registro al usuario
            register(email, password);
        });
    }

    // -----------------------------------------------------------------------------------------------------------------------------
    private void register(String email, String password) {
        tipoUsuario = UserUtil.getTipoUsuario();

        if (tipoUsuario != null && tipoUsuario.equals("candidato")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {              // Usuario se registra correctamente
                            configurarRegistroCandidato(email, password);
                            Toast.makeText(RegisterActivity.this, R.string.cuentaCreada, Toast.LENGTH_SHORT).show();

                            UserUtil.setTipoUsuario("candidato");
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.noCreada, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (tipoUsuario != null && tipoUsuario.equals("empresa")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {              // Usuario se registra correctamente
                            configurarRegistroEmpresa(email, password);
                            Toast.makeText(RegisterActivity.this, R.string.cuentaCreada, Toast.LENGTH_SHORT).show();

                            UserUtil.setTipoUsuario("empresa");
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, R.string.noCreada, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Manejar caso de error o usuario desconocido
            Toast.makeText(RegisterActivity.this, R.string.errorInesperado, Toast.LENGTH_SHORT).show();
        }
    }

    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("DiscouragedApi")
    private void configurarRegistroCandidato(String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNombre("");
        usuario.setEmail(email);
        usuario.setUsername("");
        usuario.setPassword(password);
        usuario.setTelefono("");
        usuario.setDireccion("");

        // Agregar usuario a la base de datos
        int usuarioId = controladorBD.agregarUsuario(usuario);

        Candidato candidato = new Candidato();
        candidato.setId(usuarioId);

        candidato.setExperiencia("");
        candidato.setEducacion("");

        // Agregar candidato a la base de datos
        controladorBD.agregarCandidato(candidato);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("DiscouragedApi")
    private void configurarRegistroEmpresa(String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setNombre("");
        usuario.setEmail(email);
        usuario.setUsername("");
        usuario.setPassword(password);
        usuario.setTelefono("");
        usuario.setDireccion("");

        // Agregar usuario a la base de datos
        int usuarioId = controladorBD.agregarUsuario(usuario);

        Empresa empresa = new Empresa();
        empresa.setId(usuarioId);

        empresa.setSector("");
        empresa.setWeb("");

        // Agregar empresa a la base de datos
        controladorBD.agregarEmpresa(empresa);
    }
    // -----------------------------------------------------------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------------------------------------------------------
